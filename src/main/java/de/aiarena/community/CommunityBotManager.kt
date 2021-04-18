package de.aiarena.community

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

fun main(args: Array<String>){
    SpringApplicationBuilder(CommunityBotManager::class.java).build().run(*args)
}

@SpringBootApplication
open class CommunityBotManager {
}