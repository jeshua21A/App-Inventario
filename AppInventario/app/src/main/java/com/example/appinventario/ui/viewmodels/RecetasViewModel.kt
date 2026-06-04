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

    // Estado de carga general para la UI
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Mensaje de error para mostrar al usuario
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Lista de llaveros desde Room (cache local)
    val llaveros: StateFlow<List<LlaveroEntity>> = inventarioDao.getAllLlaveros()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Lista de materiales desde Room (cache local)
    val materiales: StateFlow<List<MaterialEntity>> = inventarioDao.getAllMateriales()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Mapa que almacena los materiales asignados a cada llavero
    private val _materialesPorLlavero = MutableStateFlow<Map<Int, List<Triple<Int, MaterialEntity, Double>>>>(emptyMap())
    val materialesPorLlavero: StateFlow<Map<Int, List<Triple<Int, MaterialEntity, Double>>>> = _materialesPorLlavero.asStateFlow()
    // Carga todos los llaveros desde Supabase y los guarda en Room
    fun loadLlaveros() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
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
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar llaveros: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Carga todos los materiales desde Supabase y los guarda en Room
    fun loadMateriales(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            if (materiales.value.isNotEmpty() && !forceRefresh) {
                return@launch
            }

            _isLoading.value = true
            _errorMessage.value = null
            try {
                val materialesDto = apiService.getMateriales()
                materialesDto.forEach { dto ->
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
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar materiales: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Carga todas las recetas desde Supabase y las organiza en el mapa
    fun loadAllRecetas() {
        viewModelScope.launch {
            try {
                val recetasDto = apiService.getRecetas()
                val materialesList = materiales.value
                val materialesMap = mutableMapOf<Int, MutableList<Triple<Int, MaterialEntity, Double>>>()

                recetasDto.forEach { recetaDto ->
                    val material = materialesList.find { it.id == recetaDto.idMaterial }
                    if (material != null) {
                        if (!materialesMap.containsKey(recetaDto.idLlavero)) {
                            materialesMap[recetaDto.idLlavero] = mutableListOf()
                        }
                        // Guardar como Triple(recetaId, material, cantidad)
                        materialesMap[recetaDto.idLlavero]?.add(Triple(recetaDto.id ?: 0, material, recetaDto.cantidad))
                    }
                }

                _materialesPorLlavero.value = materialesMap
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar recetas: ${e.message}"
            }
        }
    }

    // Obtiene los materiales asignados a un llavero especifico
    fun getMaterialesForLlavero(llaveroId: Int): List<Triple<Int, MaterialEntity, Double>> {
        return _materialesPorLlavero.value[llaveroId] ?: emptyList()
    }

    // Guarda o actualiza todas las recetas de un llavero
    fun saveRecetas(llaveroId: Int, items: List<Triple<Int, Double, Double>>) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val recetasExistentes = apiService.getRecetas().filter { it.idLlavero == llaveroId }

                // items actuales: (materialId, cantidad, precio)
                val nuevosMaterialesIds = items.map { it.first }.toSet()
                val existentesMaterialesIds = recetasExistentes.map { it.idMaterial }.toSet()

                // 1. Materiales a eliminar (estaban antes pero ya no)
                val idsAEliminar = recetasExistentes.filter {
                    !nuevosMaterialesIds.contains(it.idMaterial)
                }.mapNotNull { it.id }

                idsAEliminar.forEach { recetaId ->
                    val response = apiService.deleteReceta("eq.$recetaId")
                    if (!response.isSuccessful) {
                        println("Error al eliminar receta $recetaId: ${response.errorBody()?.string()}")
                    }
                }

                // 2. Materiales a actualizar o crear
                items.forEach { (materialId, cantidad, _) ->
                    val recetaExistente = recetasExistentes.find { it.idMaterial == materialId }

                    if (recetaExistente != null) {
                        // Actualizar existente (PUT)
                        if (recetaExistente.cantidad != cantidad) {
                            val recetaActualizada = RecetaDto(
                                idLlavero = llaveroId,
                                idMaterial = materialId,
                                cantidad = cantidad
                            )
                            val recetaId = recetaExistente.id ?: return@forEach
                            val response = apiService.updateReceta("eq.$recetaId", recetaActualizada)
                            if (!response.isSuccessful) {
                                println("Error al actualizar: ${response.errorBody()?.string()}")
                            }
                        }
                    } else {
                        // Crear nueva (POST)
                        val recetaDto = RecetaDto(
                            idLlavero = llaveroId,
                            idMaterial = materialId,
                            cantidad = cantidad
                        )
                        val response = apiService.createReceta(recetaDto)
                        if (!response.isSuccessful) {
                            println("Error al crear: ${response.errorBody()?.string()}")
                        }
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


    // Añadir un nuevo material a la receta (POST)
    fun agregarMaterialAReceta(llaveroId: Int, materialId: Int, cantidad: Double) {
        viewModelScope.launch {
            try {
                val recetaDto = RecetaDto(
                    idLlavero = llaveroId,
                    idMaterial = materialId,
                    cantidad = cantidad
                )
                val response = apiService.createReceta(recetaDto)
                if (response.isSuccessful) {
                    loadAllRecetas()
                } else {
                    _errorMessage.value = "Error al agregar: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al agregar material: ${e.message}"
            }
        }
    }

    fun actualizarCantidadMaterial(recetaId: Int, llaveroId: Int, materialId: Int, nuevaCantidad: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val recetaActualizada = RecetaDto(
                    idLlavero = llaveroId,
                    idMaterial = materialId,
                    cantidad = nuevaCantidad
                )
                val response = apiService.updateReceta("eq.$recetaId", recetaActualizada)
                if (response.isSuccessful) {
                    loadAllRecetas()
                    _errorMessage.value = "Cantidad actualizada"
                } else {
                    _errorMessage.value = "Error: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Editar cantidad de un material existente (PUT)
    fun editarCantidadMaterial(recetaId: Int, llaveroId: Int, materialId: Int, nuevaCantidad: Double) {
        viewModelScope.launch {
            val recetaActualizada = RecetaDto(
                idLlavero = llaveroId,
                idMaterial = materialId,
                cantidad = nuevaCantidad
            )
            val response = apiService.updateReceta("eq.$recetaId", recetaActualizada)
            if (response.isSuccessful) {
                val mapaActual = _materialesPorLlavero.value.toMutableMap()
                val listaActual = mapaActual[llaveroId]?.toMutableList() ?: mutableListOf()
                val index = listaActual.indexOfFirst { it.first == recetaId }
                if (index != -1) {
                    val material = listaActual[index].second
                    listaActual[index] = Triple(recetaId, material, nuevaCantidad)
                    mapaActual[llaveroId] = listaActual
                    _materialesPorLlavero.value = mapaActual
                }
            }
        }
    }

    // Eliminar material de la receta (DELETE)
    fun eliminarMaterialDeReceta(recetaId: Int, llaveroId: Int) {
        viewModelScope.launch {
            val response = apiService.deleteReceta("eq.$recetaId")
            if (response.isSuccessful) {
                val mapaActual = _materialesPorLlavero.value.toMutableMap()
                val listaActual = mapaActual[llaveroId]?.toMutableList() ?: mutableListOf()
                listaActual.removeAll { it.first == recetaId }
                mapaActual[llaveroId] = listaActual
                _materialesPorLlavero.value = mapaActual
            }
        }
    }

    // Limpia el mensaje de error actual
    fun clearMessages() {
        _errorMessage.value = null
    }
}