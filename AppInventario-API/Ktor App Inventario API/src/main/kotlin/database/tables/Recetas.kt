package com.example.database.tables
import org.jetbrains.exposed.sql.Table

object Recetas : Table("receta") {
    val id = integer("id")
    val idLlavero = integer("idLlavero")
    val idMaterial = integer("idMaterial")
    val cantidad = double("cantidad")
}