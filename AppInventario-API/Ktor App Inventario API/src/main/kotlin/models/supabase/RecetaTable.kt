package com.example.models.supabase

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object RecetaTable : Table("receta"){
    val id = integer("id").autoIncrement()
    val idLlavero = integer("id_llavero").references(LlaveroTable.id, onDelete = ReferenceOption.CASCADE)
    val idMaterial = integer("id_material").references(MaterialTable.id, onDelete = ReferenceOption.CASCADE)
    val cantidad = double("cantidad")

    override val primaryKey = PrimaryKey(id)
}