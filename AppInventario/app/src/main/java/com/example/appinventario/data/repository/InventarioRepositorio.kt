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

    suspend fun guardarLlavero(llavero: LlaveroEntity) {
        if (llavero.id == 0) {
            // Es nuevo: POST
            val dto = apiService.createLlavero(llavero.toDto())
            inventarioDao.insertLlavero(dto.toEntity())
        } else {
            val dto = apiService.updateLlavero(llavero.id, llavero.toDto())
            inventarioDao.updateLlavero(dto.toEntity())
        }
    }

    suspend fun eliminarLlavero(llavero: LlaveroEntity) {
        try {
            // 1. Intentamos borrar en el servidor (Backend Ktor)
            val response = apiService.deleteLlavero(llavero.id)

            // 2. Si el servidor responde que se borró con éxito (código 200-299)
            if (response.isSuccessful) {
                // 3. Lo borramos de la base de datos local del celular
                inventarioDao.deleteLlavero(llavero)
            }
        } catch (e: Exception) {
            // Manejo de errores (por ejemplo, si no hay internet)
            e.printStackTrace()
        }
    }

    // --- AUTH ---
    suspend fun login(username: String, password: String): UsuarioEntity {
        return apiService.login(LoginDto(username, password)).toEntity()
    }
}
