package com.example.appinventario.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "materiales")
data class MaterialEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val stockActual: Double,
    val unidadMedida: String, // Ejemplo: "ml", "gramos", "unidades"
    val stockMinimo: Double,
    val precioPorUnidad: Double
)