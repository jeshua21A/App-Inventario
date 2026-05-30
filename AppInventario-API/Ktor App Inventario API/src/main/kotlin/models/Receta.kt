package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Receta (
    val id: Int? = null,
    val llaveroId: Int,
    val materialId: Int,
    val cantidadUsada: Double
)