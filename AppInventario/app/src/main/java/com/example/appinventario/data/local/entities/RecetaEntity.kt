package com.example.appinventario.data.local.entities

import androidx.room.Entity

@Entity(
    tableName = "receta",
    primaryKeys = ["llaveroId", "materialId"]
)
data class RecetaEntity(
    val llaveroId: Int,
    val materialId: Int,
    val cantidadUsada: Double
)