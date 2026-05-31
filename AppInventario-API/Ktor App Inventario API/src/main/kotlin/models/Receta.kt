package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Receta (
    val id: Int? = null,
    val idLlavero: Int,
    val idMaterial: Int,
    val cantidadUsada: Double
)