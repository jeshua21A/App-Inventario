package com.example.database.tables
import org.jetbrains.exposed.sql.Table

object Llaveros : Table("llavero") {
    val id = integer("id")
    val nombre = varchar("nombre", 100)
    val description = varchar("description", 255)
    val precioVenta = double("precioventa")

    override val primaryKey = PrimaryKey(id)
}