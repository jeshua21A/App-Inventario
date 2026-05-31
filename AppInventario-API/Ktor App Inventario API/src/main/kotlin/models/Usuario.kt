package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val id: Int? = null,
    val nombre: String,
    val psswrd: String,
    val esAdmin: Boolean
)