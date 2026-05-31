package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Llavero(
    val id: Int,
    val nombre: String,
    val description: String,
    val precioVenta: Double
)