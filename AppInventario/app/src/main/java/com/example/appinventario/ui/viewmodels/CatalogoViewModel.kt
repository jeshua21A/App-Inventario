package com.example.appinventario.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appinventario.data.local.dao.InventarioDao
import com.example.appinventario.data.local.entities.LlaveroEntity
import com.example.appinventario.data.network.InventarioApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CatalogoViewModel(
    private val inventarioDao: InventarioDao,
    private val apiService: InventarioApiService
) : ViewModel() {

    // Estados de UI
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _syncMessage = MutableStateFlow<String?>(null)
    val syncMessage: StateFlow<String?> = _syncMessage.asStateFlow()

    // Lista de llaveros desde Room (cache local)
    val listaLlaveros: StateFlow<List<LlaveroEntity>> = inventarioDao.getAllLlaveros()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Cargar llaveros desde Supabase y guardar en Room
    fun loadLlaverosFromCloud() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _syncMessage.value = "Sincronizando con la nube..."

            try {
                val llaverosDto = apiService.getLlaveros()

                llaverosDto.forEach { dto ->
                    val entity = LlaveroEntity(
                        id = dto.id ?: 0,
                        nombre = dto.nombre,
                        descripcion = dto.descripcion,
                        precioVenta = dto.precioVenta
                    )
                    inventarioDao.insertLlavero(entity)
                }

                _syncMessage.value = "${llaverosDto.size} llaveros sincronizados"
            } catch (e: Exception) {
                _errorMessage.value = "Error al sincronizar: ${e.message}"
                _syncMessage.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Probar conexion con Supabase
    fun testConnection() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val llaveros = apiService.getLlaveros()
                _errorMessage.value = "Conexión exitosa! ${llaveros.size} llaveros encontrados"
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Limpiar mensajes (para cuando se cierra el dialogo)
    fun clearMessages() {
        _errorMessage.value = null
        _syncMessage.value = null
    }
}