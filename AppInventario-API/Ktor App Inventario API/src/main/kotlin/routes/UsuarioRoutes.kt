package com.example.routes

import com.example.database.tables.Recetas
import com.example.database.tables.Usuarios
import com.example.models.Receta
import com.example.models.Usuario
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.usuarioRoutes() {
    route("/usuarios") {
        get {
            val usuarios = transaction {
                Usuarios.selectAll().map {
                    Usuario(
                        id = it[Usuarios.id],
                        nombre = it[Usuarios.nombre],
                        psswrd = it[Usuarios.psswrd],
                        esAdmin = it[Usuarios.esAdmin]
                    )
                }
            }
            call.respond(usuarios)
        }

        post {
        }
    }
}