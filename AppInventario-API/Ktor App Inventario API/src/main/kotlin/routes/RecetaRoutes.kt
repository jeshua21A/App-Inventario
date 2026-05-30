package com.example.routes

import com.example.models.Receta
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

val listaRecetas = mutableListOf<Receta>()

fun Route.recetaRoutes() {
    route("/recetas") {
        get {
            if (listaRecetas.isNotEmpty()) {
                call.respond(listaRecetas)
            } else {
                call.respondText("No hay recetas creadas", status = HttpStatusCode.NotFound)
            }
        }

        post {
            try {
                val nuevaReceta = call.receive<Receta>()
                listaRecetas.add(nuevaReceta)
                call.respond(HttpStatusCode.Created, nuevaReceta)
            } catch (e: Exception) {
                call.respondText("No hay recetas creadas todavía: ${e.localizedMessage}", status = HttpStatusCode.BadRequest)
            }
        }
    }
}