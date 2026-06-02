package com.example.appinventario.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LlaveroDto(
    val id: Int? = null,
    val nombre: String,
    val descripcion: String,
    @SerialName("precio_venta")
    val precioVenta: Double
)