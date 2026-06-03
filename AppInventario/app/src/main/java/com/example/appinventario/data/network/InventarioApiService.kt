package com.example.appinventario.data.network

import com.example.appinventario.data.remote.dto.*
import retrofit2.http.*

interface InventarioApiService {
    //LLAVEROS
    @GET("llaveros")
    suspend fun getLlaveros(): List<LlaveroDto>

    @GET("llaveros/{id}")
    suspend fun getllavero(
        @Path("id") id: Int
    ): LlaveroDto

    @POST("llaveros")
    suspend fun createLlavero(@Body llavero: LlaveroDto): LlaveroDto

    @PUT("llaveros/{id}")
    suspend fun updateLlavero(
        @Path("id") id: Int,
        @Body llavero: LlaveroDto
    ): LlaveroDto

    @DELETE("llaveros/{id}")
    suspend fun deleteLlavero(
        @Path("id") id: Int
    )

    //MATERIALES
    @GET("materiales")
    suspend fun getMateriales(): List<MaterialDto>

    @GET("materiales/{id}")
    suspend fun getMaterial(
        @Path("id") id: Int
    ): MaterialDto

    @POST("materiales")
    suspend fun createMateriales(@Body material: MaterialDto): MaterialDto

    @PUT("materiales/{id}")
    suspend fun updateMaterial(
        @Path("id") id: Int,
        @Body material: MaterialDto
    ): MaterialDto

    @DELETE("materiales/{id}")
    suspend fun deleteMaterial(
        @Path("id") id: Int
    )

    //RECETAS
    @GET("recetas")
    suspend fun getRecetas(): List<RecetaDto>

    @GET("recetas/{id}")
    suspend fun getReceta(
        @Path("id") id: Int
    ): RecetaDto

    @POST("recetas")
    suspend fun createReceta(@Body receta: RecetaDto): RecetaDto

    @DELETE("recetas/{id}")
    suspend fun deleteReceta(
        @Path("id") id: Int
    )

    // USUARIOS
    @GET("usuarios")
    suspend fun getUsuarios(): List<UsuarioDto>

    @GET("usuarios/{id}")
    suspend fun getUsuario(
        @Path("id") id: Int
    ): UsuarioDto

    @POST("usuarios")
    suspend fun createUsuario(@Body usuario: UsuarioDto): UsuarioDto

    @PUT("usuarios/{id}")
    suspend fun updateUsuario(
        @Path("id") id: Int,
        @Body usuario: UsuarioDto
    ): UsuarioDto

    @DELETE("usuarios/{id}")
    suspend fun deleteUsuario(
        @Path("id") id: Int
    )

    // LOGIN
    @POST("login")
    suspend fun login(
        @Body credenciales: LoginDto
    ): UsuarioDto
}