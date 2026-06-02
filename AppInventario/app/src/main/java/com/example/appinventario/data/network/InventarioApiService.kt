package com.example.appinventario.data.network

import com.example.appinventario.data.remote.dto.*
import retrofit2.http.*

interface InventarioApiService {
    @GET("llavero")
    suspend fun getLlaveros(): List<LlaveroDto>

    @POST("llavero")
    suspend fun createLlavero(@Body llavero: LlaveroDto): LlaveroDto

    @GET("material")
    suspend fun getMateriales(): List<MaterialDto>

    @POST("material")
    suspend fun createMateriales(@Body material: MaterialDto): MaterialDto

    @GET("receta")
    suspend fun getRecetas(): List<RecetaDto>

    @POST("receta")
    suspend fun createReceta(@Body receta: RecetaDto): RecetaDto

    @GET("usuario")
    suspend fun getUsuarios(): List<UsuarioDto>

    @POST("usuario")
    suspend fun createUsuario(@Body usuario: UsuarioDto): UsuarioDto
}