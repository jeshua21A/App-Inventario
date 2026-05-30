package com.example.routes

import com.example.models.Material
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

val listaMateriales = mutableListOf<Material>()

fun Route.materialRoutes() {
    route("/materiales") {
        get {
            if (listaMateriales.isNotEmpty()) {
                call.respond(listaMateriales)
            } else {
                call.respondText("No hay materiales en el inventario", status = HttpStatusCode.NotFound)
            }
        }

        post {
            try {
                val nuevoMaterial = call.receive<Material>()
                listaMateriales.add(nuevoMaterial)
                call.respond(HttpStatusCode.Created, nuevoMaterial)
            } catch (e: Exception) {
                call.respondText("Formato JSON inválido: ${e.localizedMessage}", status = HttpStatusCode.BadRequest)
            }
        }
    }
}