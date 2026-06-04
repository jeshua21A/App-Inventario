package com.example.appinventario.ui.viewmodels

import com.example.appinventario.data.local.entities.UsuarioEntity

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Autenticado(val usuario: UsuarioEntity) : AuthState()
    data class Error(val message: String) : AuthState()
}
