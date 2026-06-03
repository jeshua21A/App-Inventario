package com.example.appinventario.data.repository

import com.example.appinventario.data.network.ApiClient
import com.example.appinventario.data.local.dao.InventarioDao
import com.example.appinventario.data.local.entities.MaterialEntity
import com.example.appinventario.data.local.entities.UsuarioEntity
import com.example.appinventario.data.network.InventarioApiService
import com.example.appinventario.data.remote.dto.LoginDto
import com.example.appinventario.data.remote.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InventarioRepositorio @Inject constructor(
    private val apiService: InventarioApiService, // Hablar con Ktor
    private val inventarioDao: InventarioDao // Hablar con Room
) {
    val todosLosMateriales: Flow<List<MaterialEntity>> = inventarioDao.getAllInventario()

    suspend fun fetchMaterialesDesdeServidor() {
        try {
            val materialesRemote = apiService.getMateriales()

            materialesRemote.forEach { materialDto ->
                val entidadRoom = materialDto.toEntity()
                inventarioDao.insertMaterial(entidadRoom)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun login(
        username: String,
        password: String
    ): UsuarioEntity {
        return apiService.login(
            LoginDto(
                username = username,
                password = password
            )
        ).toEntity()
    }
}