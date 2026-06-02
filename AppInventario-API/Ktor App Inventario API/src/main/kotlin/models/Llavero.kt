package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Llavero(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precioVenta: Double
)