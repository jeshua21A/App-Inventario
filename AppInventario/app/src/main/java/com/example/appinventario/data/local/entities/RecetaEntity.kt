package com.example.appinventario.data.local.entities

import androidx.room.Entity

@Entity(
    tableName = "receta",
    primaryKeys = ["idLlavero", "idMaterial"]
)
data class RecetaEntity(
    val id: Int = 0,
    val idLlavero: Int,
    val idMaterial: Int,
    val cantidad: Double
)