package com.example.routes

import com.example.models.Llavero
import org.jetbrains.exposed.sql.transactions.transaction
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.llaveroRoutes() {
    route("/llaveros") {
        // Obtener todos los registros
        get {

        }

        // Obtener un registro
        get("/{id}") {

        }

        // Crear nuevo llavero
        post {

        }

        // Actuializar registro
        put("/{id}") {

        }

        // Eliminar registro
        delete("/{id}") {

        }
    }
}