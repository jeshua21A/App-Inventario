package com.example.routes

import com.example.database.dbQuery
import com.example.models.Llavero
import com.example.models.Receta
import com.example.models.Usuario
import com.example.models.supabase.LlaveroTable
import com.example.models.supabase.LlaveroTable.descripcion
import com.example.models.supabase.LlaveroTable.id
import com.example.models.supabase.LlaveroTable.nombre
import com.example.models.supabase.LlaveroTable.precioVenta
import com.example.models.supabase.UsuarioTable
import com.example.security.BCryptUtils
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import kotlin.String
import kotlin.text.toIntOrNull

val listaUsuarios = mutableListOf<Usuario>()

fun Route.usuarioRoutes() {
    route("/usuarios") {
        // Obtener todos los usuarios
        get {
            try {
                val usuario = dbQuery {
                    UsuarioTable.selectAll().map { row ->
                        Usuario(
                            id = row[UsuarioTable.id],
                            username = row[UsuarioTable.username],
                            password = row[UsuarioTable.password],
                            esAdmin = row[UsuarioTable.esAdmin]
                        )
                    }
                }
                call.respond(usuario)
            } catch (e: Exception) {
                call.respondText("Error al consultar la BD: ${e.localizedMessage}", status = HttpStatusCode.InternalServerError)
            }
        }

        // Obtener los usuarios por ID
        get("/{id}") {
            val idParam = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respondText("ID inválido", status = HttpStatusCode.BadRequest)

            try {
                val usuario = dbQuery {
                    UsuarioTable.selectAll()
                        .where { UsuarioTable.id eq idParam }
                        .map { row ->
                            Usuario(
                                id = row[UsuarioTable.id],
                                username = row[UsuarioTable.username],
                                password = row[UsuarioTable.password],
                                esAdmin = row[UsuarioTable.esAdmin]
                            )
                        }.singleOrNull()
                }
                if (usuario != null) {
                    call.respond(usuario)
                } else {
                    call.respondText("Usuario no encontrado", status = HttpStatusCode.NotFound)
                }
            } catch (e: Exception) {
                call.respondText("Error en el servidor: ${e.localizedMessage}", status = HttpStatusCode.InternalServerError)
            }
        }

        // Crear un nuevo usuario
        post {
            try {
                val input = call.receive<Usuario>()
                val passwordCifrado = BCryptUtils.hashPassword(input.password)

                val nuevoId = dbQuery {
                    UsuarioTable.insert { statement ->
                        statement[username] = input.username
                        statement[password] = passwordCifrado
                        statement[esAdmin] = input.esAdmin
                    } get UsuarioTable.id
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
                val input = call.receive<Usuario>()

                val filasActualizadas = dbQuery {
                    UsuarioTable.update({ UsuarioTable.id eq idParam }) { statement ->
                        statement[username] = input.username
                        statement[password] = input.password
                        statement[esAdmin] = input.esAdmin
                    }
                }

                if (filasActualizadas > 0) {
                    call.respond(HttpStatusCode.OK, input.copy(id = idParam))
                } else {
                    call.respondText("No se encontró ningún usuario con el ID provisto", status = HttpStatusCode.NotFound)
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
                    UsuarioTable.deleteWhere { id eq idParam }
                }

                if (filasEliminadas > 0) {
                    call.respondText("Usuario eliminado correctamente de la BD", status = HttpStatusCode.OK)
                } else {
                    call.respondText("El Usuario especificado no existe", status = HttpStatusCode.NotFound)
                }
            } catch (e: Exception) {
                call.respondText("Error al eliminar: ${e.localizedMessage}", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}