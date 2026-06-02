package com.example.routes

import com.example.database.dbQuery
import com.example.models.Llavero
import com.example.models.supabase.LlaveroTable
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

fun Route.llaveroRoutes() {
    route("/llaveros") {
        // Obtener todos los llaveros
        get {
            try {
                val llaveros = dbQuery {
                    LlaveroTable.selectAll().map { row ->
                        Llavero(
                            id = row[LlaveroTable.id],
                            nombre = row[LlaveroTable.nombre],
                            descripcion = row[LlaveroTable.descripcion],
                            precioVenta = row[LlaveroTable.precioVenta]
                        )
                    }
                }
                call.respond(llaveros)
            } catch (e: Exception) {
                call.respondText("Error al consultar la BD: ${e.localizedMessage}", status = HttpStatusCode.InternalServerError)
            }
            call.respond(llaveros)
        }
        // Obtener los llaveros por ID
        get("/{id}") {
            val idParam = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respondText("ID inválido", status = HttpStatusCode.BadRequest)

            try {
                val llavero = dbQuery {
                    LlaveroTable.selectAll()
                        .where { LlaveroTable.id eq idParam }
                        .map { row ->
                            Llavero(
                                id = row[LlaveroTable.id],
                                nombre = row[LlaveroTable.nombre],
                                descripcion = row[LlaveroTable.descripcion],
                                precioVenta = row[LlaveroTable.precioVenta]
                            )
                        }.singleOrNull()
                }
                if (llavero != null) {
                    call.respond(llavero)
                } else {
                    call.respondText("Llavero no encontrado", status = HttpStatusCode.NotFound)
                }
            } catch (e: Exception) {
                call.respondText("Error en el servidor: ${e.localizedMessage}", status = HttpStatusCode.InternalServerError)
            }
        }

        // Crear un nuevo llavero
        post {
            try {
                val input = call.receive<Llavero>()

                val nuevoId = dbQuery {
                    LlaveroTable.insert { statement ->
                        statement[nombre] = input.nombre
                        statement[descripcion] = input.descripcion
                        statement[precioVenta] = input.precioVenta
                    } get LlaveroTable.id
                }

                call.respond(HttpStatusCode.Created, input.copy(id = nuevoId))
            } catch (e: Exception) {
                call.respondText("Cuerpo JSON inválido o incompleto: ${e.localizedMessage}", status = HttpStatusCode.BadRequest)
            }
        }

        // Actualizar un llavero
        put("/{id}") {
            val idParam = call.parameters["id"]?.toIntOrNull()
                ?: return@put call.respondText("ID inválido", status = HttpStatusCode.BadRequest)

            try {
                val input = call.receive<Llavero>()

                val filasActualizadas = dbQuery {
                    LlaveroTable.update({ LlaveroTable.id eq idParam }) { statement ->
                        statement[nombre] = input.nombre
                        statement[descripcion] = input.descripcion
                        statement[precioVenta] = input.precioVenta
                    }
                }

                if (filasActualizadas > 0) {
                    call.respond(HttpStatusCode.OK, input.copy(id = idParam))
                } else {
                    call.respondText("No se encontró ningún llavero con el ID provisto", status = HttpStatusCode.NotFound)
                }
            } catch (e: Exception) {
                call.respondText("Error al actualizar: ${e.localizedMessage}", status = HttpStatusCode.InternalServerError)
            }
        }

        // Borrar un llavero
        delete("/{id}") {
            val idParam = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respondText("ID inválido", status = HttpStatusCode.BadRequest)

            try {
                val filasEliminadas = dbQuery {
                    LlaveroTable.deleteWhere { id eq idParam }
                }

                if (filasEliminadas > 0) {
                    call.respondText("Llavero eliminado correctamente de la BD", status = HttpStatusCode.OK)
                } else {
                    call.respondText("El llavero especificado no existe", status = HttpStatusCode.NotFound)
                }
            } catch (e: Exception) {
                call.respondText("Error al eliminar: ${e.localizedMessage}", status = HttpStatusCode.InternalServerError)
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