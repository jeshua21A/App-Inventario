package com.example.appinventario.ui.viewmodels

import android.graphics.ColorSpace
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appinventario.data.local.dao.InventarioDao
import com.example.appinventario.data.network.InventarioApiService

// Esta clase es el puente entre el sistema operativo y el código
// ya que android por si solo no puede saber cuales son las funciones DAO
// necesita un intérprete que le explique que hacer con los datos
// promoviendo la persistencia, seguridad y orden. De esa forma Android
// puede gestionar los objetos de manera automática
class InventarioViewModelFactory(
    private val dao: InventarioDao,
    private val apiService: InventarioApiService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventarioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventarioViewModel(dao, apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}