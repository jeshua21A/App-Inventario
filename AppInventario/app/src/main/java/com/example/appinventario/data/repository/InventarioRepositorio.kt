package com.example.appinventario.data.repository

import com.example.appinventario.data.local.dao.InventarioDao
import com.example.appinventario.data.local.entities.LlaveroEntity
import com.example.appinventario.data.local.entities.MaterialEntity
import com.example.appinventario.data.local.entities.UsuarioEntity
import com.example.appinventario.data.network.InventarioApiService
import com.example.appinventario.data.remote.dto.LoginDto
import com.example.appinventario.data.remote.mapper.toDto
import com.example.appinventario.data.remote.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InventarioRepositorio @Inject constructor(
    private val apiService: InventarioApiService,
    private val inventarioDao: InventarioDao
) {

    // --- MATERIALES ---
    val todosLosMateriales: Flow<List<MaterialEntity>> = inventarioDao.getAllMateriales()

    suspend fun sincronizarMateriales() {
        try {
            val remote = apiService.getMateriales()
            remote.forEach { inventarioDao.insertMaterial(it.toEntity()) }
        } catch (e: Exception) { e.printStackTrace() }
    }

    suspend fun agregarMaterial(material: MaterialEntity) {
        val dto = apiService.createMaterial(material.toDto())
        inventarioDao.insertMaterial(dto.toEntity())
    }

    suspend fun actualizarMaterial(material: MaterialEntity) {
        val dto = apiService.updateMaterial(material.id, material.toDto())
        inventarioDao.updateMaterial(dto.toEntity())
    }

    suspend fun eliminarMaterial(material: MaterialEntity) {
        val response = apiService.deleteMaterial(material.id)
        if (response.isSuccessful) {
            inventarioDao.deleteMaterial(material)
        }
    }

    // --- LLAVEROS ---
    val todosLosLlaveros: Flow<List<LlaveroEntity>> = inventarioDao.getAllLlaveros()

    suspend fun sincronizarLlaveros() {
        try {
            val remote = apiService.getLlaveros()
            remote.forEach { inventarioDao.insertLlavero(it.toEntity()) }
        } catch (e: Exception) { e.printStackTrace() }
    }

    suspend fun agregarLlavero(llavero: LlaveroEntity) {
        val dto = apiService.createLlavero(llavero.toDto())
        inventarioDao.insertLlavero(dto.toEntity())
    }

    suspend fun eliminarLlavero(llavero: LlaveroEntity) {
        val response = apiService.deleteLlavero(llavero.id)
        // Nota: Agregaremos deleteLlavero al DAO si es necesario, 
        // por ahora usamos la lógica de la API
    }

    // --- AUTH ---
    suspend fun login(username: String, password: String): UsuarioEntity {
        return apiService.login(LoginDto(username, password)).toEntity()
    }
}
