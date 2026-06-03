package com.example.routes

import com.example.database.dbQuery
import com.example.models.Login
import com.example.models.Usuario
import com.example.models.supabase.UsuarioTable
import com.example.security.BCryptUtils
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.jetbrains.exposed.sql.selectAll

fun Route.loginRoutes() {
    route("/login") {
        // Ingresamos el usuario para recibir su nombre de usuario y contraseña
        post {
            val credenciales = call.receive<Login>()

            val verificarUsuario = dbQuery {
                UsuarioTable.selectAll()
                    .where { UsuarioTable.username eq credenciales.username }
                    .map { row ->
                        Usuario(
                            id = row[UsuarioTable.id],
                            username = row[UsuarioTable.username],
                            password = row[UsuarioTable.password],
                            esAdmin = row[UsuarioTable.esAdmin]
                        )
                    }.singleOrNull()
            }

            if (verificarUsuario != null && BCryptUtils.checkPassword(credenciales.password, verificarUsuario.password)) {
                call.respond(HttpStatusCode.OK, verificarUsuario.copy(password = ""))
            } else {
                call.respondText("Usuario o Contraseña son incorrectos", status = HttpStatusCode.Unauthorized)
            }
        }
    }
}