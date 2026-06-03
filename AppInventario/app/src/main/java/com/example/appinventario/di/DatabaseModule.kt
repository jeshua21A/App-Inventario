package com.example.appinventario.di

import okhttp3.MediaType
import com.example.appinventario.data.network.InventarioApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun providesInventarioApiService(okHttpClient: OkHttpClient): InventarioApiService {
        val contentType = MediaType.parse("application/json; charset=UTF-8")!!
        val jsonConfig = Json { ignoreUnknownKeys = true }

        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .client(okHttpClient)
            .addConverterFactory(jsonConfig.asConverterFactory(contentType))
            .build()
            .create(InventarioApiService::class.java)
    }
}
