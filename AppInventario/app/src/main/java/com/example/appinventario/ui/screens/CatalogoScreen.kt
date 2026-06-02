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
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.appinventario.R
import com.example.appinventario.data.local.entities.LlaveroEntity
import com.example.appinventario.ui.components.*
import com.example.appinventario.ui.theme.AppColors
import com.example.appinventario.ui.theme.AppInventarioTheme
import com.example.appinventario.ui.viewmodels.CatalogoViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    viewModel: CatalogoViewModel = koinViewModel()
) {
    // Estados del ViewModel
    val listaLlaveros by viewModel.listaLlaveros.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var busquedaTexto by remember { mutableStateOf("") }
    var llaveroSeleccionado by remember { mutableStateOf<LlaveroEntity?>(null) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Cargar datos al iniciar la pantalla
    LaunchedEffect(Unit) {
        if (listaLlaveros.isEmpty()) {
            viewModel.loadLlaverosFromCloud()
        }
    }

    // Filtrar llaveros según búsqueda
    val llaverosVisibles = if (busquedaTexto.isBlank()) listaLlaveros
    else listaLlaveros.filter {
        it.nombre.contains(busquedaTexto, ignoreCase = true) ||
                it.descripcion.contains(busquedaTexto, ignoreCase = true)
    }

    // Opciones del menú lateral (cliente)
    val opcionesMenu = getOpcionesCliente(
        onCerrarSesion = { /* TODO: Implementar cierre de sesión */ }
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
                        painter = painterResource(id = R.drawable.catalogo_label),
                        contentDescription = "Catalogo",
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
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BarraBusqueda(value = busquedaTexto, onValueChange = { busquedaTexto = it })

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
                if (isLoading && listaLlaveros.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (llaverosVisibles.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Sin productos disponibles.", color = AppColors.BrownSub, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.loadLlaverosFromCloud() }) {
                                Text("Cargar desde la nube")
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(llaverosVisibles.chunked(2)) { filaProductos ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                if (filaProductos.size > 0) {
                                    ProductoCard(
                                        llavero = filaProductos[0],
                                        modifier = Modifier.weight(1f),
                                        onClick = { llaveroSeleccionado = filaProductos[0] }
                                    )
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }

                                if (filaProductos.size > 1) {
                                    ProductoCard(
                                        llavero = filaProductos[1],
                                        modifier = Modifier.weight(1f),
                                        onClick = { llaveroSeleccionado = filaProductos[1] }
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

    // Diálogo de detalles del producto
    if (llaveroSeleccionado != null) {
        ProductoDetallesDialog(
            llavero = llaveroSeleccionado!!,
            onCerrar = { llaveroSeleccionado = null }
        )
    }
}

// PREVIEW DE LA PANTALLA
@Preview(
    name = "Catalogo Screen Preview - Menu Abierto",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun CatalogoScreenPreviewMenuAbierto() {
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
            CatalogoScreen()
        }
    }
}

// PREVIEW DEL DIALOG
@Preview(
    name = "Dialogo Detalles Preview",
    showBackground = true
)
@Composable
private fun ProductoDetallesDialogPreview() {
    AppInventarioTheme {
        val productoEjemplo = LlaveroEntity(
            id = 1,
            nombre = "Llavero de cuero",
            descripcion = "Llavero de cuero personalizado con letras grabadas. Ideal para regalos y detalles especiales. Hecho con cuero 100% genuino.",
            precioVenta = 50.50
        )

        ProductoDetallesDialog(
            llavero = productoEjemplo,
            onCerrar = {}
        )
    }
}