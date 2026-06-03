package com.example.models.supabase

import org.jetbrains.exposed.sql.Table

object MaterialTable : Table("material"){
    val id = integer("id").autoIncrement()
    val nombre = varchar("nombre", 255)
    val stockActual = double("stock_actual")
    val unidadMedida = varchar("unidad_medida", 50)
    val stockMinimo = double("stock_minimo")
    val precioPorUnidad = double("precio_por_unidad")

    override val primaryKey = PrimaryKey(id)
}