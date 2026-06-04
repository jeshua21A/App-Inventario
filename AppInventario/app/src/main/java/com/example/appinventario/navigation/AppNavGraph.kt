package com.example.appinventario.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appinventario.ui.screens.*
import com.example.appinventario.ui.viewmodels.AuthState
import com.example.appinventario.ui.viewmodels.AuthViewModel
import com.example.appinventario.ui.viewmodels.InventarioViewModel
import org.koin.androidx.compose.koinViewModel

// Rutas de navegación
object Rutas {
    const val LOGIN             = "login"
    const val CATALOGO          = "catalogo"          // vista cliente (solo lectura)
    const val EDICION_CATALOGO  = "edicion_catalogo"  // admin
    const val MATERIALES        = "materiales"         // admin
    const val RECETA_LLAVEROS   = "receta_llaveros"   // admin
}

@Composable
fun AppNavGraph(
    authViewModel: AuthViewModel = koinViewModel(),
    inventarioViewModel: InventarioViewModel = koinViewModel(),
    navController: NavHostController = rememberNavController()
) {
    val authState by authViewModel.authState.collectAsState()

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
        // ── Login
        composable(Rutas.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginExitoso = {
                    // La navegación se maneja en el LaunchedEffect superior
                }
            )
        }

        // ── Catálogo (cliente, solo lectura)
        composable(Rutas.CATALOGO) {
            CatalogoScreen(
                // Si CatalogoScreen requiere su propio ViewModel, Koin lo inyectará dentro de la pantalla
                onCerrarSesion = { authViewModel.logout() }
            )
        }

        // ── Edición del catálogo (admin)
        composable(Rutas.EDICION_CATALOGO) {
            EdicionCatalogoScreen(
                onNavigateTo = { ruta -> navController.navigate(ruta) },
                onCerrarSesion = { authViewModel.logout() }
            )
        }

        // ── Materiales (admin)
        composable(Rutas.MATERIALES) {
            MaterialesScreen(
                onNavigateTo = { ruta -> navController.navigate(ruta) },
                onCerrarSesion = { authViewModel.logout() }
            )
        }

        // ── Receta Llaveros (admin)
        composable(Rutas.RECETA_LLAVEROS) {
            RecetaLlaverosScreen(
                onNavigateTo = { ruta -> navController.navigate(ruta) },
                onCerrarSesion = { authViewModel.logout() }
            )
        }
    }
}