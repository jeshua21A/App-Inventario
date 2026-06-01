package com.example.routes

import com.example.database.dbQuery
import com.example.models.Material
import com.example.models.supabase.MaterialTable
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

fun Route.materialRoutes() {
    route("/materiales") {
        // Obtener todos los materiales
        get {
            try {
                val materiales = dbQuery {
                    MaterialTable.selectAll().map { row ->
                        Material(
                            id = row[MaterialTable.id],
                            nombre = row[MaterialTable.nombre],
                            stockActual = row[MaterialTable.stockActual],
                            unidadMedida = row[MaterialTable.unidadMedida],
                            stockMinimo = row[MaterialTable.stockMinimo],
                            precioPorUnidad = row[MaterialTable.precioPorUnidad]
                        )
                    }
                }
                call.respond(materiales)
            } catch (e: Exception) {
                call.respondText("Error al consultar la BD: ${e.localizedMessage}", status = HttpStatusCode.InternalServerError)
            }
        }

        // Obtener los materiales por ID
        get("/{id}") {
            val idParam = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respondText("ID inválido", status = HttpStatusCode.BadRequest)

            try {
                val material = dbQuery {
                    MaterialTable.selectAll()
                        .where { MaterialTable.id eq idParam }
                        .map { row ->
                            Material(
                                id = row[MaterialTable.id],
                                nombre = row[MaterialTable.nombre],
                                stockActual = row[MaterialTable.stockActual],
                                unidadMedida = row[MaterialTable.unidadMedida],
                                stockMinimo = row[MaterialTable.stockMinimo],
                                precioPorUnidad = row[MaterialTable.precioPorUnidad]
                            )
                        }.singleOrNull()
                }
                if (material != null) {
                    call.respond(material)
                } else {
                    call.respondText("Material no encontrado", status = HttpStatusCode.NotFound)
                }
            } catch (e: Exception) {
                call.respondText("Error en el servidor: ${e.localizedMessage}", status = HttpStatusCode.InternalServerError)
            }
        }

        // Crear un nuevo material
        post {
            try {
                val input = call.receive<Material>()

                val nuevoId = dbQuery {
                    MaterialTable.insert { statement ->
                        statement[nombre] = input.nombre
                        statement[stockActual] = input.stockActual
                        statement[unidadMedida] = input.unidadMedida
                        statement[stockMinimo] = input.stockMinimo
                        statement[precioPorUnidad] = input.precioPorUnidad
                    } get MaterialTable.id
                }

                call.respond(HttpStatusCode.Created, input.copy(id = nuevoId))
            } catch (e: Exception) {
                call.respondText("Cuerpo JSON inválido o incompleto: ${e.localizedMessage}", status = HttpStatusCode.BadRequest)
            }
        }

        // Actualizar un material
        put("/{id}") {
            val idParam = call.parameters["id"]?.toIntOrNull()
                ?: return@put call.respondText("ID inválido", status = HttpStatusCode.BadRequest)

            try {
                val input = call.receive<Material>()

                val filasActualizadas = dbQuery {
                    MaterialTable.update({ MaterialTable.id eq idParam }) { statement ->
                        statement[nombre] = input.nombre
                        statement[stockActual] = input.stockActual
                        statement[unidadMedida] = input.unidadMedida
                        statement[stockMinimo] = input.stockMinimo
                        statement[precioPorUnidad] = input.precioPorUnidad
                    }
                }

                if (filasActualizadas > 0) {
                    call.respond(HttpStatusCode.OK, input.copy(id = idParam))
                } else {
                    call.respondText("No se encontró ningún material con el ID provisto", status = HttpStatusCode.NotFound)
                }
            } catch (e: Exception) {
                call.respondText("Error al actualizar: ${e.localizedMessage}", status = HttpStatusCode.InternalServerError)
            }
        }

        // Borrar un material
        delete("/{id}") {
            val idParam = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respondText("ID inválido", status = HttpStatusCode.BadRequest)

            try {
                val filasEliminadas = dbQuery {
                    MaterialTable.deleteWhere { id eq idParam }
                }

                if (filasEliminadas > 0) {
                    call.respondText("Material eliminado correctamente de la BD", status = HttpStatusCode.OK)
                } else {
                    call.respondText("El material especificado no existe", status = HttpStatusCode.NotFound)
                }
            } catch (e: Exception) {
                call.respondText("Error al eliminar: ${e.localizedMessage}", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}