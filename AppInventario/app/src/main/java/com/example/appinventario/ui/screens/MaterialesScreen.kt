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
import com.example.appinventario.data.local.entities.MaterialEntity
import com.example.appinventario.navigation.Rutas
import com.example.appinventario.ui.components.*
import com.example.appinventario.ui.screens.Materiales.MaterialCard
import com.example.appinventario.ui.screens.Materiales.MaterialFormDialog
import com.example.appinventario.ui.theme.AppColors
import com.example.appinventario.ui.theme.AppInventarioTheme
import com.example.appinventario.ui.viewmodels.InventarioViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialesScreen(
    onNavigateTo: (String) -> Unit,
    onCerrarSesion: () -> Unit,
    viewModel: InventarioViewModel = koinViewModel()
) {

    val materiales by viewModel.listaMateriales.collectAsState()

    var busquedaTexto by remember { mutableStateOf("") }
    var materialEditando by remember { mutableStateOf<MaterialEntity?>(null) }
    var mostrarDialogoAgregar by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val materialesVisibles = if (busquedaTexto.isBlank()) materiales
    else materiales.filter {
        it.nombre.contains(busquedaTexto, ignoreCase = true)
    }

    val opcionesMenu = getOpcionesAdmin(
        onNavigateToCatalogo = { onNavigateTo(Rutas.CATALOGO) },
        onNavigateToEdicionCatalogo = { onNavigateTo(Rutas.EDICION_CATALOGO) },
        onNavigateToMateriales = { scope.launch { drawerState.close() } },
        onNavigateToRecetas = { onNavigateTo(Rutas.RECETA_LLAVEROS) },
        onCerrarSesion = onCerrarSesion
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

                if (materialesVisibles.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Sin materiales. Agrega el primero.", color = AppColors.BrownSub, fontSize = 15.sp)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(materialesVisibles.chunked(2)) { filaMateriales ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                if (filaMateriales.size > 0) {
                                    MaterialCard(
                                        material = filaMateriales[0],
                                        modifier = Modifier.weight(1f),
                                        onEditar = { materialEditando = filaMateriales[0] }
                                    )
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }

                                if (filaMateriales.size > 1) {
                                    MaterialCard(
                                        material = filaMateriales[1],
                                        modifier = Modifier.weight(1f),
                                        onEditar = { materialEditando = filaMateriales[1] }
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

    // Diálogo para editar/agregar materiales
    if (materialEditando != null || mostrarDialogoAgregar) {
        MaterialFormDialog(
            titulo = if (materialEditando != null) "Editar Material" else "Nuevo Material",
            nombreInicial = materialEditando?.nombre ?: "",
            stockActualInicial = materialEditando?.stockActual?.toString() ?: "",
            stockMinimoInicial = materialEditando?.stockMinimo?.toString() ?: "",
            unidadMedidaInicial = materialEditando?.unidadMedida ?: "",
            precioInicial = materialEditando?.precioPorUnidad?.toString() ?: "",
            onGuardar = { nombre, stockActual, stockMinimo, unidadMedida, precio ->
                if (materialEditando != null) {
                    // TODO: viewModel.actualizarMaterial()
                    viewModel?.actualizarMaterial(
                        materialEditando!!.copy(
                            nombre = nombre,
                            stockActual = stockActual,
                            stockMinimo = stockMinimo,
                            unidadMedida = unidadMedida,
                            precioPorUnidad = precio
                        )
                    )
                } else {
                    // TODO: viewModel.agregarMaterial()
                    viewModel?.agregarMaterial(
                        nombre = nombre,
                        stock = stockActual,
                        unidad = unidadMedida,
                        minimo = stockMinimo,
                        precio = precio
                    )
                }
                materialEditando = null
                mostrarDialogoAgregar = false
            },
            onEliminar = if (materialEditando != null) {
                {
                    // TODO: viewModel.eliminarMaterial()
                    viewModel?.eliminarMaterial(materialEditando!!)
                    materialEditando = null
                }
            } else null,
            onCancelar = {
                materialEditando = null
                mostrarDialogoAgregar = false
            }
        )
    }
}