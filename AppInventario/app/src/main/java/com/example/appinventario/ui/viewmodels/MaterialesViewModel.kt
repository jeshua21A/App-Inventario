package com.example.appinventario.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appinventario.data.local.dao.InventarioDao
import com.example.appinventario.data.local.entities.MaterialEntity
import com.example.appinventario.data.network.InventarioApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MaterialesViewModel (
    private val inventarioDao: InventarioDao,
    private val inventarioApi: InventarioApiService
): ViewModel() {
    // Estados de UI
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _syncMessage = MutableStateFlow<String?>(null)
    val syncMessage: StateFlow<String?> = _syncMessage.asStateFlow()

    // Lista de materiales desde caché local
    val listaMateriles: StateFlow<List<MaterialEntity>> = inventarioDao.getAllMateriales()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    //  Cargar materiales y guardar en Room
    fun cargarMateriales(){
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _syncMessage.value = "Sincronizando con la nube..."

            try {
                val materialDto = inventarioApi.getMateriales()

                materialDto.forEach { dto ->
                    val entity = MaterialEntity(
                        id = dto.id ?: 0,
                        nombre = dto.nombre,
                        stockActual = dto.stockActual,
                        unidadMedida = dto.unidadMedida,
                        stockMinimo = dto.stockMinimo,
                        precioPorUnidad = dto.precioPorUnidad
                    )
                    inventarioDao.insertMaterial(entity)
                }

                _syncMessage.value = "${materialDto.size} materiales sincronizados"
            } catch (e: Exception) {
                _errorMessage.value = "Error al sincronizar: ${e.message}"
                _syncMessage.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Test de conexion
    fun testConexion() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val materiales = inventarioApi.getMateriales()
                _errorMessage.value = "Conexion exitosa! ${materiales.size} materiales encontrados"
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexion: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Limpiar mensajes
    fun clearMessages() {
        _errorMessage.value = null
        _syncMessage.value = null
    }
}