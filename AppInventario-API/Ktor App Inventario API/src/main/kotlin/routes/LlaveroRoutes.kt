package com.example.routes

import com.example.models.Llavero
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

val listaLlaveros = mutableListOf<Llavero>()

fun Route.llaveroRoutes() {
    route("/llaveros") {
        get {
            if (listaLlaveros.isNotEmpty()){
                call.respond(listaLlaveros)
            } else {
                call.respondText("No hay llaveros en el inventario", status = HttpStatusCode.NotFound)
            }
        }

        post {
            try {
                val nuevoLlavero = call.receive<Llavero>()
                listaLlaveros.add(nuevoLlavero)
                call.respond(HttpStatusCode.Created, nuevoLlavero)
            } catch (e: Exception) {
                call.respondText("Formato JSON inválido: ${e.localizedMessage}", status = HttpStatusCode.BadRequest)
            }
        }
    }
}