package com.example.appinventario.navigation

import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appinventario.ui.screens.*
import com.example.appinventario.ui.viewmodels.AuthState
import com.example.appinventario.ui.viewmodels.InventarioViewModel

// Rutas de navegación
object Rutas {
    const val LOGIN             = "login"
    const val CATALOGO          = "catalogo"          // vista cliente (solo lectura)
    const val EDICION_CATALOGO  = "edicion_catalogo"  // admin
    const val MATERIALES        = "materiales"         // admin
    const val RECETA_LLAVEROS   = "receta_llaveros"   // admin
}

// NavGraph principal
@Composable
fun AppNavGraph(
    viewModel: InventarioViewModel,
    navController: NavHostController = rememberNavController()
) {
    val authState by viewModel.authState.collectAsState()

    // Reaccionar al estado de auth para navegar automáticamente
    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Autenticado -> {
                val destino = if (state.usuario.esAdmin) Rutas.EDICION_CATALOGO else Rutas.CATALOGO
                navController.navigate(destino) {
                    popUpTo(Rutas.LOGIN) { inclusive = true }
                }
            }
            is AuthState.Idle -> {
                // Al cerrar sesión volvemos al login
                navController.navigate(Rutas.LOGIN) {
                    popUpTo(0) { inclusive = true }
                }
            }
            else -> Unit
        }
    }

    NavHost(
        navController = navController,
        startDestination = Rutas.LOGIN
    ) {
/*
        // ── Login
        composable(Rutas.LOGIN) {
            LoginScreen(viewModel = viewModel)
        }

        // ── Catálogo (cliente, solo lectura)
        composable(Rutas.CATALOGO) {
            CatalogoScreen(
                viewModel = viewModel,
                onCerrarSesion = { viewModel.cerrarSesion() }
            )
        }

        // ── Edición del catálogo (admin)
        composable(Rutas.EDICION_CATALOGO) {
            EdicionCatalogoScreen(
                viewModel = viewModel,
                onNavigateTo = { ruta -> navController.navigate(ruta) },
                onCerrarSesion = { viewModel.cerrarSesion() }
            )
        }

        // ── Materiales (admin)
        composable(Rutas.MATERIALES) {
            MaterialesScreen(
                viewModel = viewModel,
                onNavigateTo = { ruta -> navController.navigate(ruta) },
                onCerrarSesion = { viewModel.cerrarSesion() }
            )
        }

        // ── Receta Llaveros (admin)
        composable(Rutas.RECETA_LLAVEROS) {
            RecetaLlaverosScreen(
                viewModel = viewModel,
                onNavigateTo = { ruta -> navController.navigate(ruta) },
                onCerrarSesion = { viewModel.cerrarSesion() }
            )
        }
        */
    }
}
