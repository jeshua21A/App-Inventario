package com.example.appinventario.data.network

import com.example.appinventario.data.remote.dto.*
import retrofit2.http.*

interface InventarioApiService {
    @GET("llaveros")
    suspend fun getLlaveros(): List<LlaveroDto>

    @POST("llaveros")
    suspend fun createLlavero(@Body llavero: LlaveroDto): LlaveroDto

    @GET("materiales")
    suspend fun getMateriales(): List<MaterialDto>

    @POST("materiales")
    suspend fun createMateriales(@Body material: MaterialDto): MaterialDto

    @GET("recetas")
    suspend fun getRecetas(): List<RecetaDto>

    @POST("recetas")
    suspend fun createReceta(@Body receta: RecetaDto): RecetaDto

    @GET("usuarios")
    suspend fun getUsuarios(): List<UsuarioDto>

    @POST("usuarios")
    suspend fun createUsuario(@Body usuario: UsuarioDto): UsuarioDto
}