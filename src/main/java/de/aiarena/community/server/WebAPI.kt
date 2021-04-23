package de.aiarena.community.server

import de.aiarena.community.worker.Watchdog
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["https://ai-arena.de","http://localhost:3000"])
class WebAPI(
    @Autowired private val watchdog: Watchdog
) {

    @PostMapping("/enable/{pat}")
    open fun enable(@PathVariable pat: String){
        watchdog.enable(pat)
    }

    @PostMapping("/start")
    open fun startWatchdog(){
        watchdog.runWatchdog()
    }

    @PostMapping("/stop")
    open fun stopWatchdog(){
        watchdog.stopWatchdog()
    }
}