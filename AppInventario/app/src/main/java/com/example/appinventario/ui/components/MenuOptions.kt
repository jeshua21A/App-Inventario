package com.example.appinventario.ui.components
import com.example.appinventario.navigation.Rutas

// Opciones para ADMIN
fun getOpcionesAdmin(
    onNavigateToCatalogo: () -> Unit,
    onNavigateToEdicionCatalogo: () -> Unit,
    onNavigateToMateriales: () -> Unit,
    onNavigateToRecetas: () -> Unit,
    onCerrarSesion: () -> Unit
): List<OpcionMenu> = listOf(
    OpcionMenu("Catálogo", onNavigateToCatalogo),
    OpcionMenu("Edición Catálogo", onNavigateToEdicionCatalogo),
    OpcionMenu("Materiales", onNavigateToMateriales),
    OpcionMenu("Recetas", onNavigateToRecetas),
    OpcionMenu("Cerrar Sesión", onCerrarSesion, esDestructiva = true)
)

// Opciones para CLIENTE
fun getOpcionesCliente(
    onCerrarSesion: () -> Unit
): List<OpcionMenu> = listOf(
    OpcionMenu("Cerrar Sesión", onCerrarSesion, esDestructiva = true)
)