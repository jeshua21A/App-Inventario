@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.appinventario.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.appinventario.R
import com.example.appinventario.data.local.entities.MaterialEntity
import com.example.appinventario.navigation.Rutas
import com.example.appinventario.ui.components.*
import com.example.appinventario.ui.screens.Materiales.MaterialCard
import com.example.appinventario.ui.screens.Materiales.MaterialFormDialog
import com.example.appinventario.ui.theme.AppColors
import com.example.appinventario.ui.theme.AppInventarioTheme
import com.example.appinventario.ui.viewmodels.MaterialesViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.collections.chunked

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialesScreen(
    viewModel: MaterialesViewModel = koinViewModel(),
    navController: NavController = rememberNavController()
) {
    val materiales by viewModel.listaMateriales.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var busquedaTexto by remember { mutableStateOf("") }
    var materialSeleccionado by remember { mutableStateOf<MaterialEntity?>(null) }
    var mostrarDialogoAgregar by remember { mutableStateOf(false) }
    var isLoadingRecetas by remember { mutableStateOf(true) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit){
        if(materiales.isEmpty()){
            viewModel.cargarMateriales()
        }
    }

    // Filtrar materiales por busqueda
    val materialesFiltrados =
        if (busquedaTexto.isBlank())
            materiales
        else materiales.filter {
            it.nombre.contains(busquedaTexto, ignoreCase = true)
        }

    // Opciones del menu lateral (Admin)
    val opcionesMenu = getOpcionesAdmin(
        onNavigateToCatalogo = { navController.navigate(Rutas.CATALOGO) },
        onNavigateToEdicionCatalogo = { navController.navigate(Rutas.EDICION_CATALOGO) },
        onNavigateToMateriales = { navController.navigate(Rutas.MATERIALES) },
        onNavigateToRecetas = { },
        onCerrarSesion = { }
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimColor = Color.Transparent,
        drawerContent = {
            MenuLateral(
                drawerState = drawerState,
                opciones = opcionesMenu,
                onCerrar = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(85.dp)
                        .padding(top = 22.dp)
                        .background(AppColors.Cream)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.materiales_flag),
                        contentDescription = "Materiales",
                        modifier = Modifier.fillMaxWidth().height(75.dp),
                        contentScale = ContentScale.FillBounds
                    )

                    IconButton(
                        onClick = {
                            if (drawerState.isOpen) scope.launch { drawerState.close() }
                            else scope.launch { drawerState.open() }
                        },
                        modifier = Modifier.align(Alignment.TopStart).padding(start = 8.dp, top = 8.dp)
                    ) {
                        Icon(
                            imageVector = if (drawerState.isOpen) Icons.Default.Close else Icons.Default.Menu,
                            contentDescription = if (drawerState.isOpen) "Cerrar menú" else "Abrir menú",
                            tint = AppColors.White
                        )
                    }
                }
            },
            containerColor = AppColors.Cream
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BarraBusqueda(
                        value = busquedaTexto,
                        onValueChange = { busquedaTexto = it },
                        modifier = Modifier.weight(1f)
                    )

                    BotonAnadir(
                        onClick = { mostrarDialogoAgregar = true }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Mostrar mensaje de error si existe
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = AppColors.ErrorRed,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Mostrar loading
                if (isLoading && materiales.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Cargando materiales...", color = AppColors.BrownText, fontSize = 14.sp)
                        }
                    }
                } else if (materialesFiltrados.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Sin materiales disponibles.", color = AppColors.BrownSub, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.cargarMateriales() }) {
                                Text("Cargar desde la nube")
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(materialesFiltrados.chunked(2)) { filaMateriales ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                if (filaMateriales.size > 0) {
                                    MaterialCard(
                                        material = filaMateriales[0],
                                        modifier = Modifier.weight(1f),
                                        onEditar = { materialSeleccionado = filaMateriales[0] }
                                    )
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }

                                if (filaMateriales.size > 1) {
                                    MaterialCard(
                                        material = filaMateriales[1],
                                        modifier = Modifier.weight(1f),
                                        onEditar = { materialSeleccionado = filaMateriales[1] }
                                    )
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// PREVIEW
@Preview(
    name = "Materiales Screen Preview",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun MaterialesScreenPreview() {
    AppInventarioTheme {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)

        ModalNavigationDrawer(
            drawerState = drawerState,
            scrimColor = Color.Transparent,
            drawerContent = {
                MenuLateral(
                    drawerState = drawerState,
                    opciones = listOf(
                        OpcionMenu("Cerrar Sesión", {}, esDestructiva = true)
                    ),
                    onCerrar = {}
                )
            }
        ) {
            MaterialesScreen()
        }
    }
}
/*
@Preview(
    name = "Material Form Dialog - Modo Añadir",
    showBackground = true
)
@Composable
private fun MaterialFormDialogAddPreview() {
    AppInventarioTheme {
        MaterialFormDialog(
            titulo = "Nuevo Material",
            nombreInicial = "",
            stockActualInicial = "",
            stockMinimoInicial = "",
            unidadMedidaInicial = "",
            precioInicial = "",
            onGuardar = { nombre, stockActual, stockMinimo, unidadMedida, precio ->
                println("Añadir: $nombre - Stock: $stockActual - Precio: $precio")
            },
            onEliminar = null,
            onCancelar = {
                println("Cancelar")
            }
        )
    }
}
 */

// PREVIEW del Dialog
@Preview(
    name = "Material Form Dialog - Modo Editar",
    showBackground = true
)
@Composable
private fun MaterialFormDialogEditPreview() {
    AppInventarioTheme {
        MaterialFormDialog(
            titulo = "Editar Material",
            nombreInicial = "Herrajes",
            stockActualInicial = "10",
            stockMinimoInicial = "10",
            unidadMedidaInicial = "cm",
            precioInicial = "50.50",
            onGuardar = { nombre, stockActual, stockMinimo, unidadMedida, precio ->
                println("Editar: $nombre - Stock: $stockActual - Precio: $precio")
            },
            onEliminar = {
                println("Eliminar material")
            },
            onCancelar = {
                println("Cancelar")
            }
        )
    }
}