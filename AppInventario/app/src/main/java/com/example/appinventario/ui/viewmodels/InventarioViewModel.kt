package com.example.appinventario.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appinventario.data.local.dao.InventarioDao
import com.example.appinventario.data.local.entities.MaterialEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InventarioViewModel(private val inventarioDao: InventarioDao): ViewModel() {

    // 1. Observer todos los materiales para confirmar cuando habra algún cambio
    val listaMateriales: StateFlow<List<MaterialEntity>> = inventarioDao.getAllMateriales()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 2. Función para insertar un nuevo material
    fun agregarMaterial(nombre: String, stock: Double, unidad: String, minimo: Double, precio: Double){
        viewModelScope.launch {
            val nuevo = MaterialEntity(
                nombre = nombre,
                stockActual = stock,
                unidadMedida = unidad,
                stockMinimo = minimo,
                precioPorUnidad = precio
            )
            inventarioDao.insertMaterial(nuevo)
        }
    }
}