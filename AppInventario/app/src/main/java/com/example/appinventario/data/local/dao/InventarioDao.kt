package com.example.appinventario.data.local.dao

import androidx.room.*
import com.example.appinventario.data.local.entities.LlaveroEntity
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

    @Query("SELECT * FROM materiales ORDER BY nombre ASC")
    fun getAllMateriales(): Flow<List<MaterialEntity>>

    @Query("SELECT * FROM materiales WHERE stockActual <= stockMinimo")
    fun getMaterialesEnEscasez(): Flow<List<MaterialEntity>>

    // Descontar stock
    @Query("UPDATE materiales SET stockActual = stockActual - :cantidad WHERE id = :id")
    suspend fun reduceStock(id: Int, cantidad: Double)

    // --- Operaciones con Llaveros ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLlavero(llavero: LlaveroEntity)

    @Query("SELECT * FROM llaveros")
    fun getAllLlaveros(): Flow<List<LlaveroEntity>>

    // --- Operaciones con Recetas ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredientesReceta(receta: RecetaEntity)

    // Obtener los materiales de un llavero específico
    @Query("""
        SELECT materiales.* FROM materiales
        INNER JOIN recetas ON materiales.id = recetas.materialId
        WHERE recetas.llaveroId = :llaveroId
    """)
    fun getMaterialesDeUnLlavero(llaveroId: Int): Flow<List<MaterialEntity>>

    // --- Operaciones con Usuario ---

    // Para el admin
    @Query("SELECT * FROM materiales")
    fun getAllInventario(): Flow<List<MaterialEntity>>

    // Para el cliente
    @Query("SELECT nombre, precioVenta FROM llaveros")
    fun getCatalogoPublico(): Flow<List<LlaveroEntity>>
}