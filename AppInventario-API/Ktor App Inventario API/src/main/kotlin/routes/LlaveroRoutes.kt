package com.example.routes

import com.example.database.tables.Llaveros
import com.example.models.Llavero

import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

import io.ktor.http.*
import io.ktor.server.request.*
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

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
            val id = call.parameters["id"]?.toIntOrNull()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@get
            }

            val llavero = transaction {

                Llaveros.selectAll()
                    .where { Llaveros.id eq id }
                    .map {
                        Llavero(
                            id = it[Llaveros.id],
                            nombre = it[Llaveros.nombre],
                            description = it[Llaveros.description],
                            precioVenta = it[Llaveros.precioVenta]
                        )
                    }
                    .singleOrNull()
            }

            if (llavero == null)
                call.respond(HttpStatusCode.NotFound, "Llavero no encontrado")
            else
                call.respond(llavero)
        }

        // Crear nuevo llavero
        post {
            val nuevoLlavero = call.receive<Llavero>()

            transaction {

                Llaveros.insert {

                    it[id] = nuevoLlavero.id
                    it[nombre] = nuevoLlavero.nombre
                    it[description] = nuevoLlavero.description
                    it[precioVenta] = nuevoLlavero.precioVenta
                }
            }

            call.respond(
                HttpStatusCode.Created,
                "Llavero creado correctamente"
            )
        }

        // Actuializar registro
        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@put
            }

            val llaveroActualizado = call.receive<Llavero>()

            val filasActualizadas = transaction {

                Llaveros.update({ Llaveros.id eq id }) {

                    it[nombre] = llaveroActualizado.nombre
                    it[description] = llaveroActualizado.description
                    it[precioVenta] = llaveroActualizado.precioVenta
                }
            }

            if (filasActualizadas == 0)
                call.respond(HttpStatusCode.NotFound, "Llavero no encontrado")
            else
                call.respond(HttpStatusCode.OK, "Llavero actualizado")
        }

        // Eliminar registro
        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@delete
            }

            val filasEliminadas = transaction {

                Llaveros.deleteWhere {
                    Llaveros.id eq id
                }
            }

            if (filasEliminadas == 0)
                call.respond(HttpStatusCode.NotFound, "Llavero no encontrado")
            else
                call.respond(HttpStatusCode.OK, "Llavero eliminado")
        }
    }
}