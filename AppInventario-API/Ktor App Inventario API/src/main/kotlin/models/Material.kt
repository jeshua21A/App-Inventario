package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Material (
    val id: Int? = null,
    val nombre: String,
    val stockActual: Double,
    val unidadMedida: String,
    val stockMinimo: Double,
    val precioPorUnidad: Double
)