package com.example.appinventario.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appinventario.data.repository.InventarioRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repositorio: InventarioRepositorio
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(user: String, pass: String) {
        if (user.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.Error("Usuario y contraseña son obligatorios")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val usuario = repositorio.login(user, pass)
                _authState.value = AuthState.Autenticado(usuario)
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Credenciales incorrectas o error de conexión")
            }
        }
    }

    fun logout() {
        _authState.value = AuthState.Idle
    }
}
