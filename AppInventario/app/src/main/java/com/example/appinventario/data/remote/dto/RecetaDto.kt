package com.example.appinventario.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecetaDto(
    val id: Int? = null,
    val idLlavero: Int,
    val idMaterial: Int,
    val cantidad: Double
)