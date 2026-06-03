package com.example.models.supabase

import org.jetbrains.exposed.sql.Table

object LlaveroTable : Table("llavero"){
    val id = integer("id").autoIncrement()
    val nombre = varchar("nombre", 255)
    val descripcion = varchar("descripcion", 500)
    val precioVenta = double("precio_venta")

    override val primaryKey = PrimaryKey(id)
}