package com.example.database.tables
import org.jetbrains.exposed.sql.Table

object Materiales : Table("material") {
    val id = integer("id")
    val nombre = varchar("nombre", 100)
    val stockActual = double("stockActual")
    val unidadMedida = varchar("unidadMedida", 100)
    val stockMinimo = double("stockMinimo")
    val precioPorUnidad = double("precioPorUnidad")
}