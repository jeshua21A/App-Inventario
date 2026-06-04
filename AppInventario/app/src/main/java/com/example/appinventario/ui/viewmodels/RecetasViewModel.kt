package com.example.appinventario.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appinventario.data.local.dao.InventarioDao
import com.example.appinventario.data.local.entities.LlaveroEntity
import com.example.appinventario.data.local.entities.MaterialEntity
import com.example.appinventario.data.network.InventarioApiService
import com.example.appinventario.data.remote.dto.RecetaDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecetasViewModel(
    private val inventarioDao: InventarioDao,
    private val apiService: InventarioApiService
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    val llaveros: StateFlow<List<LlaveroEntity>> = inventarioDao.getAllLlaveros()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val materiales: StateFlow<List<MaterialEntity>> = inventarioDao.getAllMateriales()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _materialesPorLlavero = MutableStateFlow<Map<Int, List<Pair<MaterialEntity, Double>>>>(emptyMap())
    val materialesPorLlavero: StateFlow<Map<Int, List<Pair<MaterialEntity, Double>>>> = _materialesPorLlavero.asStateFlow()

    fun loadLlaveros() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val llaverosDto = apiService.getLlaveros()
                llaverosDto.forEach { dto ->
                    inventarioDao.insertLlavero(LlaveroEntity(
                        id = dto.id ?: 0,
                        nombre = dto.nombre,
                        descripcion = dto.descripcion,
                        precioVenta = dto.precioVenta
                    ))
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar llaveros: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMateriales(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            if (materiales.value.isNotEmpty() && !forceRefresh) return@launch
            _isLoading.value = true
            try {
                val materialesDto = apiService.getMateriales()
                materialesDto.forEach { dto ->
                    inventarioDao.insertMaterial(MaterialEntity(
                        id = dto.id ?: 0,
                        nombre = dto.nombre,
                        stockActual = dto.stockActual,
                        unidadMedida = dto.unidadMedida,
                        stockMinimo = dto.stockMinimo,
                        precioPorUnidad = dto.precioPorUnidad
                    ))
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar materiales: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadAllRecetas() {
        viewModelScope.launch {
            try {
                val recetasDto = apiService.getRecetas()
                val materialesList = materiales.value
                val materialesMap = mutableMapOf<Int, MutableList<Pair<MaterialEntity, Double>>>()

                recetasDto.forEach { recetaDto ->
                    val material = materialesList.find { it.id == recetaDto.idMaterial }
                    if (material != null) {
                        materialesMap.getOrPut(recetaDto.idLlavero) { mutableListOf() }
                            .add(material to recetaDto.cantidad)
                    }
                }
                _materialesPorLlavero.value = materialesMap
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar recetas: ${e.message}"
            }
        }
    }

    fun getMaterialesForLlavero(llaveroId: Int): List<Pair<MaterialEntity, Double>> {
        return _materialesPorLlavero.value[llaveroId] ?: emptyList()
    }

    fun saveRecetas(llaveroId: Int, items: List<Triple<Int, Double, Double>>) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val recetasExistentes = apiService.getRecetas().filter { it.idLlavero == llaveroId }
                val nuevosMaterialesIds = items.map { it.first }.toSet()

                // 1. Eliminar materiales que ya no están
                recetasExistentes.filter { !nuevosMaterialesIds.contains(it.idMaterial) }
                    .forEach { receta ->
                        receta.id?.let { apiService.deleteReceta(it) }
                    }

                // 2. Actualizar o Crear usando parámetros nombrados
                items.forEach { (materialId, cantidad, _) ->
                    val recetaExistente = recetasExistentes.find { it.idMaterial == materialId }

                    if (recetaExistente != null) {
                        if (recetaExistente.cantidad != cantidad) {
                            val dto = RecetaDto(
                                idLlavero = llaveroId,
                                idMaterial = materialId,
                                cantidad = cantidad
                            )
                            apiService.updateReceta(recetaExistente.id!!, dto)
                        }
                    } else {
                        val dto = RecetaDto(
                            idLlavero = llaveroId,
                            idMaterial = materialId,
                            cantidad = cantidad
                        )
                        apiService.createReceta(dto)
                    }
                }

                loadAllRecetas()
                _errorMessage.value = "Recetas guardadas exitosamente"
            } catch (e: Exception) {
                _errorMessage.value = "Error al guardar recetas: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateRecetaCantidad(recetaId: Int, nuevaCantidad: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val todasRecetas = apiService.getRecetas()
                val recetaActual = todasRecetas.find { it.id == recetaId }

                if (recetaActual != null) {
                    val recetaActualizada = RecetaDto(
                        idLlavero = recetaActual.idLlavero,
                        idMaterial = recetaActual.idMaterial,
                        cantidad = nuevaCantidad
                    )
                    // CORRECCIÓN: Pasar recetaId directamente como Int
                    apiService.updateReceta(recetaId, recetaActualizada)
                    loadAllRecetas()
                    _errorMessage.value = "Cantidad actualizada"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteReceta(recetaId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // CORRECCIÓN: Pasar recetaId directamente como Int
                val response = apiService.deleteReceta(recetaId)
                if (response.isSuccessful) {
                    loadAllRecetas()
                    _errorMessage.value = "Receta eliminada"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al eliminar receta: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearMessages() {
        _errorMessage.value = null
    }
}