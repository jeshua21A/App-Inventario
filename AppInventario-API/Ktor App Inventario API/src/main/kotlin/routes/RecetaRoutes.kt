package com.example.routes

import com.example.database.dbQuery
import com.example.models.Llavero
import com.example.models.Receta
import com.example.models.supabase.LlaveroTable
import com.example.models.supabase.LlaveroTable.descripcion
import com.example.models.supabase.LlaveroTable.id
import com.example.models.supabase.LlaveroTable.nombre
import com.example.models.supabase.LlaveroTable.precioVenta
import com.example.models.supabase.RecetaTable
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import kotlin.Int

val listaRecetas = mutableListOf<Receta>()

fun Route.recetaRoutes() {
    route("/recetas") {
        // Obtener todas las recetas
        get {
            try {
                val recetas = dbQuery {
                    RecetaTable.selectAll().map { row ->
                        Receta(
                            id = row[RecetaTable.id],
                            idLlavero = row[RecetaTable.idLlavero],
                            idMaterial = row[RecetaTable.idMaterial],
                            cantidad = row[RecetaTable.cantidad]
                        )
                    }
                }
                call.respond(recetas)
            } catch (e: Exception) {
                call.respondText("Error al consultar la BD: ${e.localizedMessage}", status = HttpStatusCode.InternalServerError)
            }
            call.respond(recetas)
        }

        // Crear un nuevo llavero
        post {
            try {
                val input = call.receive<Receta>()

                val nuevoId = dbQuery {
                    RecetaTable.insert { statement ->
                        statement[idLlavero] = input.idLlavero
                        statement[idMaterial] = input.idMaterial
                        statement[cantidad] = input.cantidad
                    } get RecetaTable.id
                }

                call.respond(HttpStatusCode.Created, input.copy(id = nuevoId))
            } catch (e: Exception) {
                call.respondText("Cuerpo JSON inválido o incompleto: ${e.localizedMessage}", status = HttpStatusCode.BadRequest)
            }
        }
    }
}