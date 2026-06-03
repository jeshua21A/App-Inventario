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
import androidx.compose.ui.text.font.FontWeight
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
import com.example.appinventario.data.local.entities.MaterialEntity
import com.example.appinventario.navigation.Rutas
import com.example.appinventario.ui.components.*
import com.example.appinventario.ui.screens.Receta.RecetaFormDialog
import com.example.appinventario.ui.screens.Receta.RecetaCard
import com.example.appinventario.ui.screens.Receta.MaterialAsignadoCard
import com.example.appinventario.ui.theme.AppColors
import com.example.appinventario.ui.theme.AppInventarioTheme
import com.example.appinventario.ui.viewmodels.InventarioViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// TODO: Data class temporal para Preview (reemplazar con datos de Room)
data class RecetaPreview(
    val llavero: LlaveroEntity,
    val materiales: List<MaterialEntity>,
    val cantidades: Map<Int, Double>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecetaLlaverosScreen(
    // TODO: Cambiar: viewModel debe ser obligatorio cuando se implemente en la app real
    viewModel: InventarioViewModel? = null,

    // TODO: Cambiar: navController debe venir de la navegacion real
    navController: NavController = rememberNavController(),

    // TODO: Cambiar: listaLlaveros debe venir del ViewModel real
    listaLlaveros: StateFlow<List<LlaveroEntity>>? = null,

    // TODO: Cambiar: listaMateriales debe venir del ViewModel real
    listaMateriales: StateFlow<List<MaterialEntity>>? = null
) {
    // TODO: Quitar: datos simulados SOLO para Preview
    val previewLlaveros = remember {
        MutableStateFlow(
            listOf(
                LlaveroEntity(1, "Llavero de cuero", "Llavero personalizado", 50.50),
                LlaveroEntity(2, "Llavero metalico", "Llavero de metal grabado", 45.00)
            )
        )
    }

    val previewMateriales = remember {
        MutableStateFlow(
            listOf(
                MaterialEntity(1, "Cuero", 100.0, "cm", 10.0, 5.0),
                MaterialEntity(2, "Anilla metalica", 50.0, "pza", 5.0, 2.0),
                MaterialEntity(3, "Grabado laser", 30.0, "pza", 5.0, 10.0)
            )
        )
    }

    // TODO: Cambiar: en la app real, usar viewModel.listaLlaveros y viewModel.listaMateriales
    val llaverosFlow = listaLlaveros ?: previewLlaveros
    val materialesFlow = listaMateriales ?: previewMateriales
    val llaveros by llaverosFlow.collectAsState()
    val materiales by materialesFlow.collectAsState()

    var busquedaTexto by remember { mutableStateOf("") }
    var recetaSeleccionada by remember { mutableStateOf<LlaveroEntity?>(null) }
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
        onNavigateToEdicionCatalogo = { navController.navigate(Rutas.EDICION_CATALOGO) },
        onNavigateToMateriales = { navController.navigate(Rutas.MATERIALES) },
        onNavigateToRecetas = { /* ya estas aqui */ },
        onCerrarSesion = { viewModel?.cerrarSesion() ?: Unit }
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

                if (llaverosVisibles.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Sin recetas. Agrega la primera.", color = AppColors.BrownSub, fontSize = 15.sp)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(llaverosVisibles) { llavero ->
                            // TODO: Obtener materiales reales del llavero con viewModel.getMaterialesDeUnLlavero()
                            RecetaCard(
                                llavero = llavero,
                                materiales = emptyList(), // TODO: Reemplazar con datos reales
                                onEditar = { recetaSeleccionada = llavero }
                            )
                        }
                    }
                }
            }
        }
    }

    // Dialogo para editar/agregar receta
    if (recetaSeleccionada != null || mostrarDialogoAgregar) {
        RecetaFormDialog(
            titulo = if (recetaSeleccionada != null) "Editar Receta" else "Nueva Receta",
            llavero = recetaSeleccionada,
            listaMateriales = materiales,
            // TODO: Obtener materiales asignados actualmente
            materialesAsignados = emptyList(),
            onGuardar = { llaveroId, items ->
                // TODO: viewModel.agregarIngredienteReceta() para cada item
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

// PREVIEW DE LA PANTALLA
@Preview(
    name = "Receta Llaveros Screen Preview",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun RecetaLlaverosScreenPreview() {
    AppInventarioTheme {
        RecetaLlaverosScreen()
    }
}

// ==================== PREVIEWS DE LAS CARDS ====================

@Preview(
    name = "Receta Card - Con Materiales",
    showBackground = true
)
@Composable
private fun RecetaCardWithMaterialsPreview() {
    AppInventarioTheme {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = AppColors.Cream
        ) {
            val llaveroEjemplo = LlaveroEntity(
                id = 1,
                nombre = "Llavero de cuero",
                descripcion = "Llavero personalizado con letras grabadas. Ideal para regalos y detalles especiales.",
                precioVenta = 50.50
            )

            val materialesEjemplo = listOf(
                MaterialEntity(1, "Cuero", 100.0, "cm", 10.0, 5.0) to 15.0,
                MaterialEntity(2, "Anilla metalica", 50.0, "pza", 5.0, 2.0) to 1.0,
                MaterialEntity(3, "Grabado laser", 30.0, "pza", 5.0, 10.0) to 1.0
            )

            RecetaCard(
                llavero = llaveroEjemplo,
                materiales = materialesEjemplo,
                onEditar = {}
            )
        }
    }
}

@Preview(
    name = "Receta Card - Sin Materiales",
    showBackground = true
)
@Composable
private fun RecetaCardWithoutMaterialsPreview() {
    AppInventarioTheme {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = AppColors.Cream
        ) {
            val llaveroEjemplo = LlaveroEntity(
                id = 2,
                nombre = "Llavero metalico",
                descripcion = "Llavero de metal grabado con diseno personalizado",
                precioVenta = 45.00
            )

            RecetaCard(
                llavero = llaveroEjemplo,
                materiales = emptyList(),
                onEditar = {}
            )
        }
    }
}

// ==================== PREVIEWS DEL DIALOGO ====================

@Preview(
    name = "Receta Form Dialog - Modo Añadir",
    showBackground = true
)
@Composable
private fun RecetaFormDialogAddPreview() {
    AppInventarioTheme {
        val llaveroEjemplo = LlaveroEntity(
            id = 1,
            nombre = "Llavero de cuero",
            descripcion = "Llavero personalizado con letras grabadas",
            precioVenta = 50.50
        )

        val materialesEjemplo = listOf(
            MaterialEntity(1, "Cuero", 100.0, "cm", 10.0, 5.0),
            MaterialEntity(2, "Anilla metalica", 50.0, "pza", 5.0, 2.0),
            MaterialEntity(3, "Grabado laser", 30.0, "pza", 5.0, 10.0)
        )

        RecetaFormDialog(
            titulo = "Nueva Receta",
            llavero = llaveroEjemplo,
            listaMateriales = materialesEjemplo,
            materialesAsignados = emptyList(),
            onGuardar = { llaveroId, items ->
                println("Guardar receta para llavero $llaveroId con ${items.size} materiales")
            },
            onCancelar = {
                println("Cancelar")
            }
        )
    }
}

@Preview(
    name = "Receta Form Dialog - Modo Editar",
    showBackground = true
)
@Composable
private fun RecetaFormDialogEditPreview() {
    AppInventarioTheme {
        val llaveroEjemplo = LlaveroEntity(
            id = 1,
            nombre = "Llavero de cuero",
            descripcion = "Llavero personalizado con letras grabadas",
            precioVenta = 50.50
        )

        val materialesEjemplo = listOf(
            MaterialEntity(1, "Cuero", 100.0, "cm", 10.0, 5.0),
            MaterialEntity(2, "Anilla metalica", 50.0, "pza", 5.0, 2.0),
            MaterialEntity(3, "Grabado laser", 30.0, "pza", 5.0, 10.0)
        )

        val materialesAsignados = listOf(
            materialesEjemplo[0] to 15.0,
            materialesEjemplo[1] to 1.0
        )

        RecetaFormDialog(
            titulo = "Editar Receta",
            llavero = llaveroEjemplo,
            listaMateriales = materialesEjemplo,
            materialesAsignados = materialesAsignados,
            onGuardar = { llaveroId, items ->
                println("Guardar receta para llavero $llaveroId con ${items.size} materiales")
            },
            onCancelar = {
                println("Cancelar")
            }
        )
    }
}

// ==================== PREVIEW DE MATERIAL ASIGNADO CARD ====================

@Preview(
    name = "Material Asignado Card",
    showBackground = true
)
@Composable
private fun MaterialAsignadoCardPreview() {
    AppInventarioTheme {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = AppColors.Cream
        ) {
            val materialEjemplo = MaterialEntity(
                id = 1,
                nombre = "Cuero",
                stockActual = 100.0,
                unidadMedida = "cm",
                stockMinimo = 10.0,
                precioPorUnidad = 5.0
            )

            MaterialAsignadoCard(
                material = materialEjemplo,
                cantidad = 15.0,
                onCantidadCambio = { nuevaCantidad ->
                    println("Cantidad cambiada: $nuevaCantidad")
                },
                onEliminar = {
                    println("Eliminar material")
                }
            )
        }
    }
}