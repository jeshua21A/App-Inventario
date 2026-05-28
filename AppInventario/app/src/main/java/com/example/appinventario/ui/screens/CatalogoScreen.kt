@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.appinventario.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
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
import com.example.appinventario.ui.components.*
import com.example.appinventario.ui.theme.AppColors
import com.example.appinventario.ui.theme.AppInventarioTheme
import com.example.appinventario.ui.viewmodels.InventarioViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    // TODO: Cambiar: viewModel debe ser obligatorio cuando se implemente en la app real
    viewModel: InventarioViewModel? = null,

    // TODO: Cambiar: navController debe venir de la navegación real
    // TODO: Quitar: rememberNavController es solo para Preview
    navController: NavController = rememberNavController(),

    // TODO: Cambiar: listaLlaveros debe venir del ViewModel real
    // TODO: Quitar: el valor por defecto null es solo para Preview
    listaLlaveros: StateFlow<List<LlaveroEntity>>? = null
) {
    // TODO: Quitar: estos datos simulados SOLO son para Preview
    // TODO: Reemplazar: cuando viewModel no sea null, usar viewModel.listaLlaveros
    val previewData = remember {
        MutableStateFlow(
            listOf(
                LlaveroEntity(1, "Llavero de cuero", "Llavero de cuero personalizado", 50.50),
                LlaveroEntity(2, "Llavero metálico", "Llavero de metal grabado", 45.00),
                LlaveroEntity(3, "Llavero acrílico", "Llavero acrílico transparente", 35.50),
                LlaveroEntity(4, "Llavero madera", "Llavero artesanal de madera", 60.00)
            )
        )
    }

    // TODO: Cambiar: en la app real, usar solo viewModel.listaLlaveros
    val llaverosFlow = listaLlaveros ?: previewData
    val llaveros by llaverosFlow.collectAsState()

    var busquedaTexto by remember { mutableStateOf("") }
    var llaveroSeleccionado by remember { mutableStateOf<LlaveroEntity?>(null) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val llaverosVisibles = if (busquedaTexto.isBlank()) llaveros
    else llaveros.filter {
        it.nombre.contains(busquedaTexto, ignoreCase = true) ||
                it.descripcion.contains(busquedaTexto, ignoreCase = true)
    }

    // TODO: Cambiar: viewModel.cerrarSesion() debe llamar al método real
    // TODO: Quitar: el operador ?. es solo porque viewModel es opcional en Preview
    val opcionesMenu = getOpcionesCliente(
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

                if (llaverosVisibles.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Sin productos disponibles.", color = AppColors.BrownSub, fontSize = 15.sp)
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

    // Dialogo de detalles del producto
    if (llaveroSeleccionado != null) {
        ProductoDetallesDialog(
            llavero = llaveroSeleccionado!!,
            onCerrar = { llaveroSeleccionado = null }
        )
    }
}

//  PREVIEW DE LA PANTALLA

@Preview(
    name = "Catalogo Screen Preview - Menu Abierto",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun CatalogoScreenPreviewMenuAbierto() {
    AppInventarioTheme {
        // Forzar drawer abierto para Preview
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