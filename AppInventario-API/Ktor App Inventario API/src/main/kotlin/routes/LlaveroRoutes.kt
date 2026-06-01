package com.example.routes

import com.example.database.tables.Llaveros
import com.example.models.Llavero
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import io.ktor.http.*
import io.ktor.server.request.*

fun Route.llaveroRoutes() {
    route("/llaveros") {
        // Obtener todos los registros
        get {
            val llaveros = transaction {
                Llaveros.selectAll().map {
                    Llavero(
                        id = it[Llaveros.id],
                        nombre = it[Llaveros.nombre],
                        description = it[Llaveros.description],
                        precioVenta = it[Llaveros.precioVenta]
                    )
                }
            }
            call.respond(llaveros)
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