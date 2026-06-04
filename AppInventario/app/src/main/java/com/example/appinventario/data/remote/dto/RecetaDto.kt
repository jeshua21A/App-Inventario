package com.example.appinventario.data.remote.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class RecetaDto(
    val id: Int? = null,
    @SerialName("id_llavero")
    val idLlavero: Int,
    @SerialName("id_material")
    val idMaterial: Int,
    val cantidad: Double
)