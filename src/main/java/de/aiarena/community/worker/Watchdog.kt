package de.aiarena.community.worker

import com.mashape.unirest.http.JsonNode
import com.mashape.unirest.http.Unirest
import de.aiarena.community.config.BotConfigurations
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

@Component
class Watchdog(
    @Autowired private val configuration: BotConfigurations
) : Runnable{
    private var isActive = false
    private lateinit var pat: String
    private val processedKeys = ArrayList<String>()

    fun enable(pat: String){
        this.pat = pat
    }

    private fun loadAvailableGames(){
        val response = Unirest.get("http://hub.localhost/openslots")
            .header("pat",pat)
            .asJson()

        println("RESPONSE: "+response.body)
        for(i in 0 until response.body.array.length()){
            val item = response.body.array.getJSONObject(i)
            if(!item.isNull("Token")){
                val token = item.getString("Token")
                if(!processedKeys.contains(token)){
                    processToken(item)
                }
            }
        }

        val keysToDelete = ArrayList<String>()
        keysToDelete.addAll(processedKeys)
        for(i in 0 until response.body.array.length()){
            val item = response.body.array.getJSONObject(i)
            if(!item.isNull("Token")){
                keysToDelete.remove(item.getString("Token"))
            }
        }
        processedKeys.removeAll(keysToDelete)
    }

    private fun processToken(data: JSONObject){
        val token = data.getString("Token")
        val game = data.getString("Game")
        processedKeys.add(token)
        val runConfiguration = configuration.getConfiguration(game)
            ?: return

        val builder = ProcessBuilder(
            (runConfiguration.command + " "+token).split(" ")
        )
            .inheritIO()
            .directory(File(runConfiguration.workDir))
        builder.start()
    }

    fun runWatchdog(){
        Thread{run()}.start()
    }
    fun stopWatchdog(){
        isActive = false
    }

    override fun run() {
        if(isActive){return}
        isActive = true
        while(isActive){
            loadAvailableGames()
            Thread.sleep(10000)
        }
    }
}