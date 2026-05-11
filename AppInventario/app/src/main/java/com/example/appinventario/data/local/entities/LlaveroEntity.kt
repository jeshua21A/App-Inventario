package com.example.appinventario.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "llavero")
data class LlaveroEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val precioVenta: Double
)

data class LlaveroPublico (
    val nombre: String,
    val precioVenta: Double
)