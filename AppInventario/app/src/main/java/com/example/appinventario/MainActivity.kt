package com.example.appinventario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appinventario.data.local.database.AppDatabase
import com.example.appinventario.ui.screens.MaterialesScreen
import com.example.appinventario.ui.theme.AppInventarioTheme
import com.example.appinventario.ui.viewmodels.InventarioViewModel
import com.example.appinventario.ui.viewmodels.InventarioViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Obtener la instancia de la base de datos nativa SQLite
        val database = AppDatabase.getDatabase(this)
        // 2. Obtener el DAO (el mensajero entre Kotlin y SQL
        val dao = database.inventarioDao()
        // 3. Crear la "Fábrica" pasándole el DAO
        val factory = InventarioViewModelFactory(dao)

        enableEdgeToEdge()
        setContent {
            AppInventarioTheme {
                // Creamos o recuperamos el ViewModel
                val inventarioViewModel: InventarioViewModel = viewModel( factory = factory)
                //--- Ejemplos para probar screen por screen:
                //MaterialesScreen(viewModel = inventarioViewModel)

                //viewModel = inventarioViewModel,
                //navController = rememberNavController()

                //--Probar proyecto completo
                // Requiere los siguientes cambios adicionales:
                // 1. Crear las rutas en AppNavGraph.kt
                // 2. Conectar LoginScreen con viewModel.login() y los demas screens
                // 3. Crear Pantallas faltantes
                //
                // AppNavGraph(viewModel = inventarioViewModel)

                // Llamamos a la pantalla principal de Admin como prueba
                MaterialesScreen(viewModel = inventarioViewModel)
            }
        }
    }
}
