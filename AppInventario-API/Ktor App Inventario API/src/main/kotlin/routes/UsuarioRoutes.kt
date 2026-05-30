package com.example.routes

import com.example.models.Receta
import com.example.models.Usuario
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

val listaUsuarios = mutableListOf<Usuario>()

fun Route.usuarioRoutes() {
    route("/usuarios") {
        get {
            if (listaUsuarios.isNotEmpty()) {
                call.respond(listaUsuarios)
            } else {
                call.respondText("No hay recetas creadas", status = HttpStatusCode.NotFound)
            }
        }

        post {
            try {
                val nuevoUsuario = call.receive<Usuario>()
                listaUsuarios.add(nuevoUsuario)
                call.respond(HttpStatusCode.Created, nuevoUsuario)
            } catch (e: Exception) {
                call.respondText("No hay recetas creadas todavía: ${e.localizedMessage}", status = HttpStatusCode.BadRequest)
            }
        }
    }
}