package com.example.appinventario.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MaterialDto(
    val id: Int? = null,
    val nombre: String,
    val stockActual: Double,
    val unidadMedida: String,
    val stockMinimo: Double,
    val precioPorUnidad: Double
)