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
import com.example.appinventario.ui.screens.Receta.RecetaFormDialog
import com.example.appinventario.ui.screens.Receta.RecetaCard
import com.example.appinventario.ui.theme.AppColors
import com.example.appinventario.ui.theme.AppInventarioTheme
import com.example.appinventario.ui.viewmodels.RecetasViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecetaLlaverosScreen(
    onNavigateTo: (String) -> Unit,
    onCerrarSesion: () -> Unit,
    viewModel: RecetasViewModel = koinViewModel()
) {

    val llaveros by viewModel.llaveros.collectAsState()
    val materiales by viewModel.materiales.collectAsState()
    val materialesPorLlavero by viewModel.materialesPorLlavero.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var busquedaTexto by remember { mutableStateOf("") }
    var recetaSeleccionada by remember { mutableStateOf<LlaveroEntity?>(null) }
    var mostrarDialogoAgregar by remember { mutableStateOf(false) }
    var isLoadingRecetas by remember { mutableStateOf(true) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Cargar datos al iniciar
    LaunchedEffect(Unit) {
        isLoadingRecetas = true
        if (llaveros.isEmpty()) {
            viewModel.loadLlaveros()
        }
        if (materiales.isEmpty()) {
            viewModel.loadMateriales()
        }
        viewModel.loadAllRecetas()
        isLoadingRecetas = false
    }

    // Verificar si las recetas se estan cargando
    val isRecetasLoading = materialesPorLlavero.isEmpty() && !isLoading && llaveros.isNotEmpty()

    val llaverosVisibles = if (busquedaTexto.isBlank()) llaveros
    else llaveros.filter {
        it.nombre.contains(busquedaTexto, ignoreCase = true) ||
                it.descripcion.contains(busquedaTexto, ignoreCase = true)
    }

    val opcionesMenu = getOpcionesAdmin(
        onNavigateToCatalogo = { onNavigateTo(Rutas.CATALOGO) },
        onNavigateToEdicionCatalogo = { onNavigateTo(Rutas.EDICION_CATALOGO) },
        onNavigateToMateriales = { onNavigateTo(Rutas.MATERIALES) },
        onNavigateToRecetas = { scope.launch { drawerState.close() } },
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
                        painter = painterResource(id = R.drawable.receta_llaveros_flag),
                        contentDescription = "Recetas",
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
                            contentDescription = if (drawerState.isOpen) "Cerrar menu" else "Abrir menu",
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

                // Mensaje de error
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = AppColors.ErrorRed,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Pantalla de carga inicial
                if (isLoading && llaveros.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Cargando llaveros...", color = AppColors.BrownText, fontSize = 14.sp)
                        }
                    }
                }
                // Pantalla de carga de recetas
                else if (isRecetasLoading && llaveros.isNotEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Cargando recetas...", color = AppColors.BrownText, fontSize = 14.sp)
                        }
                    }
                }
                // Lista vacia
                else if (llaverosVisibles.isEmpty() && !isLoading) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Sin recetas. Agrega la primera.", color = AppColors.BrownSub, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.loadLlaveros() }) {
                                Text("Cargar desde la nube")
                            }
                        }
                    }
                }
                // Lista de recetas
                else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(llaverosVisibles) { llavero ->
                            RecetaCard(
                                llavero = llavero,
                                materiales = viewModel.getMaterialesForLlavero(llavero.id),
                                onEditar = { recetaSeleccionada = llavero }
                            )
                        }
                    }
                }
            }
        }
    }

    // Dialogo para editar/agregar receta con pantalla de carga
    if (recetaSeleccionada != null || mostrarDialogoAgregar) {
        // Pantalla de carga mientras se cargan los materiales
        if (materiales.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .width(240.dp)
                        .height(140.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = AppColors.Cream)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Cargando materiales disponibles...",
                            color = AppColors.BrownText,
                            fontSize = 13.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            RecetaFormDialog(
                titulo = if (recetaSeleccionada != null) "Editar Receta" else "Nueva Receta",
                llavero = recetaSeleccionada,
                listaLlaveros = llaveros,  // <- Este parametro es obligatorio
                listaMateriales = materiales,
                materialesAsignados = recetaSeleccionada?.let {
                    viewModel.getMaterialesForLlavero(it.id)
                } ?: emptyList(),
                onGuardar = { llaveroId, items ->
                    viewModel.saveRecetas(llaveroId, items)
                    recetaSeleccionada = null
                    mostrarDialogoAgregar = false
                },
                onCancelar = {
                    recetaSeleccionada = null
                    mostrarDialogoAgregar = false
                }
            )
        }
    }
}
