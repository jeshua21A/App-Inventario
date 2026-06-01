package com.example.database.tables
import org.jetbrains.exposed.sql.Table

object Materiales : Table("material") {
    val id = integer("id")
    val nombre = varchar("nombre", 100)
    val stockActual = double("stockactual")
    val unidadMedida = varchar("unidadmedida", 100)
    val stockMinimo = double("stockminimo")
    val precioPorUnidad = double("precioporunidad")

    override val primaryKey = PrimaryKey(id)
}