package com.example.routes

import com.example.database.tables.Materiales
import com.example.models.Material
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import io.ktor.server.request.*

fun Route.materialRoutes() {
    route("/materiales") {
        get {
            val materiales = transaction {
                Materiales.selectAll().map {
                    Material(
                        id = it[Materiales.id],
                        nombre = it[Materiales.nombre],
                        stockActual = it[Materiales.stockActual],
                        unidadMedida = it[Materiales.unidadMedida],
                        stockMinimo = it[Materiales.stockMinimo],
                        precioPorUnidad = it[Materiales.precioPorUnidad]
                    )
                }
            }
            call.respond(materiales)
        }

        post {
        }
    }
}