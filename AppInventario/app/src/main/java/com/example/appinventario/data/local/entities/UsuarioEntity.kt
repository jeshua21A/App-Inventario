package com.example.appinventario.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
data class UsuarioEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val user: String,
    val password: String,
    val esAdmin: Boolean
)