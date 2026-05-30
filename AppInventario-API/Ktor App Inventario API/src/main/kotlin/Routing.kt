package com.example

import com.example.routes.llaveroRoutes
import com.example.routes.materialRoutes
import com.example.routes.recetaRoutes
import com.example.routes.usuarioRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("API del Sistema App Inventario en Funcionamiento")
        }
        // Módulos de los endpoints
        llaveroRoutes()
        materialRoutes()
        recetaRoutes()
        usuarioRoutes()
    }
}