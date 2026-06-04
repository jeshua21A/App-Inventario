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
import androidx.navigation.compose.rememberNavController
import com.example.appinventario.data.local.database.AppDatabase
import com.example.appinventario.navigation.AppNavGraph
import com.example.appinventario.ui.screens.CatalogoScreen
import com.example.appinventario.ui.screens.EdicionCatalogoScreen
import com.example.appinventario.ui.screens.MaterialesScreen
import com.example.appinventario.ui.screens.RecetaLlaverosScreen
import com.example.appinventario.ui.theme.AppInventarioTheme
import com.example.appinventario.ui.viewmodels.AuthViewModel
import com.example.appinventario.ui.viewmodels.InventarioViewModel
import com.example.appinventario.ui.viewmodels.InventarioViewModelFactory
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Nota: Ya no necesitamos crear manualmente la base de datos, DAO ni Factory
        // Koin se encarga de esto automáticamente
        /*
        // 1. Obtener la instancia de la base de datos nativa SQLite
        val database = AppDatabase.getDatabase(this)
        // 2. Obtener el DAO (el mensajero entre Kotlin y SQL
        val dao = database.inventarioDao()
        // 3. Crear la "Fábrica" pasándole el DAO
        val factory = InventarioViewModelFactory(dao)
        */

        enableEdgeToEdge()
        setContent {
            AppInventarioTheme {
                // Obtenemos los ViewModels desde Koin
                val authViewModel: AuthViewModel = koinViewModel()
                val inventarioViewModel: InventarioViewModel =
                    koinViewModel()

                val navController = rememberNavController()

                // Usamos el NavGraph para controlar qué pantalla mostrar
                AppNavGraph(
                    authViewModel = authViewModel,
                    inventarioViewModel = inventarioViewModel,
                    navController = navController
                )
            }
        }
    }
}