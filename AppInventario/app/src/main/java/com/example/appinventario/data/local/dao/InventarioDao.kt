package com.example.appinventario.data.local.dao

import androidx.room.*
import com.example.appinventario.data.local.entities.LlaveroEntity
import com.example.appinventario.data.local.entities.LlaveroPublico
import com.example.appinventario.data.local.entities.MaterialEntity
import com.example.appinventario.data.local.entities.RecetaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InventarioDao {

    // --- Operaciones con Materiales ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterial(material: MaterialEntity)

    @Update
    suspend fun updateMaterial(material: MaterialEntity)

    @Delete
    suspend fun deleteMaterial(material: MaterialEntity)

    @Query("SELECT * FROM material ORDER BY nombre ASC")
    fun getAllMateriales(): Flow<List<MaterialEntity>>

    @Query("SELECT * FROM material WHERE stockActual <= stockMinimo")
    fun getMaterialesEnEscasez(): Flow<List<MaterialEntity>>

    @Query("UPDATE material SET stockActual = stockActual - :cantidad WHERE id = :id")
    suspend fun reduceStock(id: Int, cantidad: Double)

    // --- Operaciones con Llaveros ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLlavero(llavero: LlaveroEntity)

    @Update
    suspend fun updateLlavero(llavero: LlaveroEntity)

    @Delete
    suspend fun deleteLlavero(llavero: LlaveroEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLlaveros(llaveros: List<LlaveroEntity>)

    @Query("SELECT * FROM llavero")
    fun getAllLlaveros(): Flow<List<LlaveroEntity>>

    // --- Operaciones con Recetas ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredienteReceta(receta: RecetaEntity)

    @Delete
    suspend fun deleteReceta(receta: RecetaEntity)

    @Query("DELETE FROM receta WHERE idLlavero = :idLlavero")
    suspend fun deleteRecetasPorLlavero(idLlavero: Int)

    @Query("""
        SELECT material.* FROM material
        INNER JOIN receta ON material.id = receta.idMaterial
        WHERE receta.idLlavero = :idLlavero
    """)
    fun getMaterialesDeUnLlavero(idLlavero: Int): Flow<List<MaterialEntity>>

    // --- Consultas Especializadas ---
    @Query("SELECT * FROM material")
    fun getAllInventario(): Flow<List<MaterialEntity>>

    @Query("SELECT nombre, precioVenta FROM llavero")
    fun getCatalogoPublico(): Flow<List<LlaveroPublico>>
}
