package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val id: Int? = null,
    val user: String,
    val password: String,
    val esAdmin: Boolean
)