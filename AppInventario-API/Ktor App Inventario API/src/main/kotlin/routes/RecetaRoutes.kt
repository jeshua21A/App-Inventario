package com.example.routes

import com.example.database.tables.Recetas
import com.example.models.Receta
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import io.ktor.http.*
import io.ktor.server.request.*

fun Route.recetaRoutes() {
    route("/recetas") {
        get {
            val recetas = transaction {
                Recetas.selectAll().map {
                    Receta(
                        id = it[Recetas.id],
                        idLlavero = it[Recetas.idLlavero],
                        idMaterial = it[Recetas.idMaterial],
                        cantidadUsada = it[Recetas.cantidad]
                    )
                }
            }
            call.respond(recetas)
        }

        post {
        }
    }
}