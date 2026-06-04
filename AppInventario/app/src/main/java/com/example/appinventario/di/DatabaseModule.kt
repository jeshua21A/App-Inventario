package com.example.appinventario.di

import com.example.appinventario.data.local.database.AppDatabase
import com.example.appinventario.data.network.InventarioApiService
import com.example.appinventario.data.repository.InventarioRepositorio
import com.example.appinventario.ui.viewmodels.AuthViewModel
import com.example.appinventario.ui.viewmodels.InventarioViewModel
import com.example.appinventario.ui.viewmodels.CatalogoViewModel
import com.example.appinventario.ui.viewmodels.RecetasViewModel
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType
import org.koin.android.ext.koin.androidContext

private const val BASE_URL = "http://10.0.2.2:8081"

val appModule = module {
    // 1. Room Database
    single {
        AppDatabase.getDatabase(androidContext())
    }

    // 2. DAO
    single {
        get<AppDatabase>().inventarioDao()
    }

    // 3. OkHttpClient con interceptores
    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    // 4. API Service
    @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
    single<InventarioApiService> {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
            encodeDefaults = false
            explicitNulls = false
            isLenient = true
        }

        Retrofit.Builder()
            .baseUrl("$BASE_URL/")
            .client(get<OkHttpClient>())
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(InventarioApiService::class.java)
    }

    // 5. Repositorio
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
    // viewModel de Catalogo
    viewModel {
        CatalogoViewModel(
            inventarioDao = get(),
            apiService = get()
        )
    }
    //View Model de Recetas
    viewModel {
        RecetasViewModel(
            inventarioDao = get(),
            apiService = get()
        )
    }

    // Auth ViewModel
    viewModel {
        AuthViewModel(repositorio = get())
    }
}