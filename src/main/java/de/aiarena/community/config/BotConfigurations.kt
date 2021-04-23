package de.aiarena.community.config

import com.mashape.unirest.http.JsonNode
import org.json.JSONObject
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.nio.file.Files

data class RunConfiguration(val command: String, val workDir: String)

@Component
class BotConfigurations {
    private val configurations: JSONObject
    init {
        configurations = JSONObject(
            Files.readAllLines(File("bots.json").toPath()).joinToString("")
        )
    }

    fun getConfiguration(game: String) : RunConfiguration?{
        if(!configurations.has(game)){
            return null
        }
        return RunConfiguration(
            configurations.getJSONObject(game).getString("command"),
            configurations.getJSONObject(game).getString("work-directory")
        )
    }
}
