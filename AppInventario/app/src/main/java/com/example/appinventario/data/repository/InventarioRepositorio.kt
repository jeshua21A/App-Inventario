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

    // UNIFICADO: Alta y Modificación de Materiales
    suspend fun guardarMaterial(material: MaterialEntity) {
        try {
            if (material.id == 0) {
                val dto = apiService.createMaterial(material.toDto())
                inventarioDao.insertMaterial(dto.toEntity())
            } else {
                val dto = apiService.updateMaterial(material.id, material.toDto())
                inventarioDao.updateMaterial(dto.toEntity())
            }
        } catch (e: Exception) { e.printStackTrace() }
    }

    suspend fun eliminarMaterial(material: MaterialEntity) {
        try {
            val response = apiService.deleteMaterial(material.id)
            if (response.isSuccessful) {
                inventarioDao.deleteMaterial(material)
            }
        } catch (e: Exception) { e.printStackTrace() }
    }

    // --- LLAVEROS ---
    val todosLosLlaveros: Flow<List<LlaveroEntity>> = inventarioDao.getAllLlaveros()

    suspend fun sincronizarLlaveros() {
        try {
            val remote = apiService.getLlaveros()
            remote.forEach { inventarioDao.insertLlavero(it.toEntity()) }
        } catch (e: Exception) { e.printStackTrace() }
    }

    // UNIFICADO: Alta y Modificación de Llaveros
    suspend fun guardarLlavero(llavero: LlaveroEntity) {
        try {
            if (llavero.id == 0) {
                val dto = apiService.createLlavero(llavero.toDto())
                inventarioDao.insertLlavero(dto.toEntity())
            } else {
                val dto = apiService.updateLlavero(llavero.id, llavero.toDto())
                inventarioDao.updateLlavero(dto.toEntity())
            }
        } catch (e: Exception) { e.printStackTrace() }
    }

    suspend fun eliminarLlavero(llavero: LlaveroEntity) {
        // Quitamos el try-catch interno para que el ViewModel pueda capturar el error
        val response = apiService.deleteLlavero(llavero.id)

        if (response.isSuccessful) {
            // Solo si la API confirma el borrado, lo quitamos del celular
            inventarioDao.deleteLlavero(llavero)
        } else {
            // Si el servidor falla (ej. error 500 o 404), lanzamos el error
            throw Exception("El servidor no pudo eliminar el producto (Error ${response.code()})")
        }
    }

    // --- AUTH ---
    suspend fun login(username: String, password: String): UsuarioEntity {
        return apiService.login(LoginDto(username, password)).toEntity()
    }
}
