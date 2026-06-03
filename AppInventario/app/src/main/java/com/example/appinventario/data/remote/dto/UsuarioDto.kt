package com.example.appinventario.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioDto(
    val id: Int? = null,
    val username: String,
    val password: String,
    val esAdmin: Boolean
)