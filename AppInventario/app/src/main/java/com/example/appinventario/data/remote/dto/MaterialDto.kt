package com.example.appinventario.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MaterialDto(
    val id: Int? = null,
    val nombre: String,
    @SerialName("stock_actual")
    val stockActual: Double,
    @SerialName("unidad_medida")
    val unidadMedida: String,
    @SerialName("stock_minimo")
    val stockMinimo: Double,
    @SerialName("precio_por_unidad")
    val precioPorUnidad: Double
)