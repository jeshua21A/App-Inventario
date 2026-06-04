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
import com.example.appinventario.navigation.Rutas
import com.example.appinventario.ui.components.*
import com.example.appinventario.ui.screens.Edicion.ProductoCardEditable
import com.example.appinventario.ui.screens.Edicion.LlaveroFormDialog
import com.example.appinventario.ui.theme.AppColors
import com.example.appinventario.ui.theme.AppInventarioTheme
import com.example.appinventario.ui.viewmodels.CatalogoViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EdicionCatalogoScreen(
    navController: NavController,
    viewModel: CatalogoViewModel
) {

    val llaveros by viewModel.listaLlaveros.collectAsState()

    var busquedaTexto by remember { mutableStateOf("") }
    var llaveroEditando by remember { mutableStateOf<LlaveroEntity?>(null) }
    var mostrarDialogoAgregar by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val llaverosVisibles = if (busquedaTexto.isBlank()) llaveros
    else llaveros.filter {
        it.nombre.contains(busquedaTexto, ignoreCase = true) ||
                it.descripcion.contains(busquedaTexto, ignoreCase = true)
    }

    val opcionesMenu = getOpcionesAdmin(
        onNavigateToCatalogo = { navController.navigate(Rutas.CATALOGO) },
        onNavigateToEdicionCatalogo = { scope.launch { drawerState.close() } },
        onNavigateToMateriales = { navController.navigate(Rutas.MATERIALES) },
        onNavigateToRecetas = { navController.navigate(Rutas.RECETA_LLAVEROS) },
        onCerrarSesion = {
            navController.navigate(Rutas.LOGIN) {
                popUpTo(0)
            }
        }
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
                        painter = painterResource(id = R.drawable.edicion_catalogo_flag),
                        contentDescription = "Edicion Catalogo",
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

                if (llaverosVisibles.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Sin productos. Agrega el primero.", color = AppColors.BrownSub, fontSize = 15.sp)
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
                                    ProductoCardEditable(
                                        producto = filaProductos[0],
                                        modifier = Modifier.weight(1f),
                                        onEditar = { llaveroEditando = filaProductos[0] }
                                    )
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }

                                if (filaProductos.size > 1) {
                                    ProductoCardEditable(
                                        producto = filaProductos[1],
                                        modifier = Modifier.weight(1f),
                                        onEditar = { llaveroEditando = filaProductos[1] }
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

    if (llaveroEditando != null || mostrarDialogoAgregar) {
        LlaveroFormDialog(
            titulo = if (llaveroEditando != null) "Editar Producto" else "Nuevo Producto",
            nombreInicial = llaveroEditando?.nombre ?: "",
            descInicial = llaveroEditando?.descripcion ?: "",
            precioInicial = llaveroEditando?.precioVenta?.toString() ?: "",
            onGuardar = { nombre, descripcion, precio ->
                val precioDouble = precio ?: 0.0

                if (llaveroEditando != null) {
                    viewModel.agregarLlaveroNuevo(
                        llaveroEditando!!.copy(
                            nombre = nombre,
                            descripcion = descripcion,
                            precioVenta = precioDouble
                        )
                    )
                } else {
                    viewModel.agregarLlaveroNuevo(
                        LlaveroEntity(
                            id = 0,
                            nombre = nombre,
                            descripcion = descripcion,
                            precioVenta = precioDouble
                        )
                    )
                }
                llaveroEditando = null
                mostrarDialogoAgregar = false
            },
            onEliminar = if (llaveroEditando != null) {
                {
                    llaveroEditando = null
                }
            } else null,
            onCancelar = {
                llaveroEditando = null
                mostrarDialogoAgregar = false
            }
        )
    }
}
