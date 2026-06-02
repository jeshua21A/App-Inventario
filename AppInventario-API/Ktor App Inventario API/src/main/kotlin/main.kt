package com.example

import com.example.database.DatabaseFactory
import io.ktor.server.engine.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    DatabaseFactory.init()
    embeddedServer(
        factory = io.ktor.server.netty.Netty,
        port = 8081,
        host = "0.0.0.0",
        module = Application::rootModule
    ).start(wait = true)
}
