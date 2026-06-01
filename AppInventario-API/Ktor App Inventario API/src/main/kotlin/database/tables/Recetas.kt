package com.example.database.tables
import org.jetbrains.exposed.sql.Table

object Recetas : Table("receta") {
    val id = integer("id")
    val idLlavero = integer("idllavero").references(Llaveros.id)
    val idMaterial = integer("idmaterial").references(Materiales.id)
    val cantidad = double("cantidad")

    override val primaryKey = PrimaryKey(id)
}