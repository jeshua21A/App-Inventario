package com.example.appinventario.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appinventario.data.local.entities.LlaveroEntity
import com.example.appinventario.data.local.entities.LlaveroPublico
import com.example.appinventario.data.repository.InventarioRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CatalogoViewModel(
    private val repositorio: InventarioRepositorio
) : ViewModel() {

    // Estados de UI para control de carga y mensajes
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _syncMessage = MutableStateFlow<String?>(null)
    val syncMessage: StateFlow<String?> = _syncMessage.asStateFlow()

    // Observamos los llaveros directamente desde el Repositorio (Reactividad total)
    val listaLlaveros: StateFlow<List<LlaveroEntity>> = repositorio.todosLosLlaveros
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Si el repositorio no expone catalogoPublico, podrías añadirlo allí.
    // Por ahora, lo mantenemos como flujo si la vista lo requiere.
    val catalogoPublico: StateFlow<List<LlaveroPublico>> = MutableStateFlow<List<LlaveroPublico>>(emptyList())
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Al iniciar, sincronizamos datos automáticamente
        loadLlaverosFromCloud()
    }

    // Sincronización: Pide al repo que traiga datos de la API y los guarde en Room
    fun loadLlaverosFromCloud() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _syncMessage.value = "Sincronizando con el servidor..."

            try {
                repositorio.sincronizarLlaveros()
                _syncMessage.value = "Catálogo actualizado correctamente"
            } catch (e: Exception) {
                _errorMessage.value = "Error de sincronización: ${e.localizedMessage}"
                _syncMessage.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ALTA y MODIFICACIÓN: Esta función ahora impacta en API y Room
    fun agregarLlaveroNuevo(nuevoLlavero: LlaveroEntity) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // El Repositorio decide si hace POST o PUT y actualiza Room
                repositorio.guardarLlavero(nuevoLlavero)
                _syncMessage.value = "Producto guardado con éxito"
            } catch (e : Exception) {
                _errorMessage.value = "Error al guardar: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // BAJA: Elimina en API y luego en Room
    fun eliminarLlavero(llavero: LlaveroEntity) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repositorio.eliminarLlavero(llavero)
                _syncMessage.value = "Producto eliminado"
            } catch (e: Exception) {
                _errorMessage.value = "Error al eliminar: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Limpiar mensajes para la UI
    fun clearMessages() {
        _errorMessage.value = null
        _syncMessage.value = null
    }
}