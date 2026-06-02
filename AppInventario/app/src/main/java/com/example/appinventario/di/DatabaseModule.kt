package com.example.appinventario.di

import com.example.appinventario.data.local.database.AppDatabase
import com.example.appinventario.data.network.InventarioApiService
import com.example.appinventario.data.repository.InventarioRepositorio
import com.example.appinventario.ui.viewmodels.InventarioViewModel
import com.example.appinventario.ui.viewmodels.CatalogoViewModel
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType
import org.koin.android.ext.koin.androidContext

private const val SUPABASE_URL = "https://aytckcllbulgfvkavhzt.supabase.co"
private const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImF5dGNrY2xsYnVsZ2Z2a2F2aHp0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3Nzk4MDQ2ODksImV4cCI6MjA5NTM4MDY4OX0.GTc7UGBW-9RMibAd9wzOAcfJXLUTYUfuNrAhOsmplBc"

val appModule = module {
    // 1. Room Database
    single {
        AppDatabase.getDatabase(androidContext())
    }

    // 2.DAO
    single {
        get<AppDatabase>().inventarioDao()
    }

    // 3. OkHttpClient con interceptores
    single {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(chain.request().newBuilder()
                    .addHeader("apikey", SUPABASE_ANON_KEY)
                    .addHeader("Authorization", "Bearer $SUPABASE_ANON_KEY")
                    .addHeader("Content-Type", "application/json")
                    .build())
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    // 4. API Service
    single {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }

        Retrofit.Builder()
            .baseUrl("$SUPABASE_URL/rest/v1/")
            .client(get())
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(InventarioApiService::class.java)
    }

    // 5.Repositorio
    single {
        InventarioRepositorio(
            apiService = get(),
            inventarioDao = get()
        )
    }

    // 6. ViewModels
    viewModel {
        InventarioViewModel(
            inventarioDao = get(),
            apiService = get()
        )
    }
    // Catalogo viewModel
    viewModel {
        CatalogoViewModel(
            inventarioDao = get(),
            apiService = get()
        )
    }
}