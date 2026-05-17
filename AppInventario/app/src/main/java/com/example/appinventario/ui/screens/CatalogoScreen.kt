package com.example.appinventario.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Close
import com.example.appinventario.R
import com.example.appinventario.ui.screens.components.catalogo.BarraBusqueda
import com.example.appinventario.ui.screens.components.catalogo.ProductoCard
import com.example.appinventario.ui.screens.components.catalogo.ProductoDetalles
import com.example.appinventario.ui.screens.components.catalogo.MenuLateral
import com.example.appinventario.ui.theme.AppInventarioTheme
import kotlinx.coroutines.launch

// Simulacion de los atributos que seran extraidos de la base de datos
data class Llavero(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double
)

@Composable
fun CatalogoScreen(
    onProductoClick: (Llavero) -> Unit = {},
    onCerrarSesion: () -> Unit = {},
    mostrarMenuEnPreview: Boolean = false
) {
    var busquedaTexto by remember { mutableStateOf("") }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Estado para el dialogo (se abre al hacer click en un producto)
    var productoSeleccionado by remember { mutableStateOf<Llavero?>(null) }

    // Control para mostrar menu en preview
    val mostrarMenu = mostrarMenuEnPreview

    // DATOS SIMULADOS
    val productos = listOf(
        Llavero(1, "Llavero de cuero", "Llavero de cuero personalizado con letras grabadas. Ideal para regalos y detalles especiales. Hecho con cuero 100% genuino.", 50.50),
        Llavero(2, "Llavero metalico", "Llavero de metal grabado con diseno personalizado. Resistente y duradero.", 45.00),
        Llavero(3, "Llavero acrilico", "Llavero acrilico transparente con foto impresa. Perfecto para fotos de mascotas.", 35.50),
        Llavero(4, "Llavero madera", "Llavero artesanal de madera de olivo. Pieza unica y ecologica.", 60.00),
        Llavero(5, "Llavero con iniciales", "Llavero de cuero con iniciales grabadas en metal dorado.", 55.00),
        Llavero(6, "Llavero multifuncional", "Llavero con abrebotellas y destapador. Practico y funcional.", 40.00)
    )

    val productosFiltrados = if (busquedaTexto.isBlank()) {
        productos
    } else {
        productos.filter { producto ->
            producto.nombre.contains(busquedaTexto, ignoreCase = true) ||
                    producto.descripcion.contains(busquedaTexto, ignoreCase = true)
        }
    }

    // Contenido principal de la pantalla
    val contenidoPrincipal = @Composable {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFAED))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(85.dp)
                    .padding(top = 22.dp)
                    .background(Color(0xFFFFFAED))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.catalogo_label),
                    contentDescription = "Catalogo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(75.dp)
                )

                IconButton(
                    onClick = {
                        if (mostrarMenu) {
                            if (drawerState.isOpen) {
                                scope.launch { drawerState.close() }
                            } else {
                                scope.launch { drawerState.open() }
                            }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 8.dp, top = 8.dp)
                ) {
                    Icon(
                        imageVector = if (drawerState.isOpen) Icons.Default.Close else Icons.Default.Menu,
                        contentDescription = if (drawerState.isOpen) "Cerrar" else "Menu",
                        tint = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(top = 55.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(35.dp))

                // Barra de busqueda
                BarraBusqueda(
                    value = busquedaTexto,
                    onValueChange = { busquedaTexto = it }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Lista de productos
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    val productosEnFilas = productosFiltrados.chunked(2)

                    items(productosEnFilas) { filaProductos ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            if (filaProductos.size > 0) {
                                ProductoCard(
                                    producto = filaProductos[0],
                                    onClick = { productoSeleccionado = filaProductos[0] },
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }

                            if (filaProductos.size > 1) {
                                ProductoCard(
                                    producto = filaProductos[1],
                                    onClick = { productoSeleccionado = filaProductos[1] },
                                    modifier = Modifier.weight(1f)
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

    // Navigation Drawer (solo si mostrarMenu es true)
    if (mostrarMenu) {
        // Abrir el menu automaticamente en preview
        LaunchedEffect(Unit) {
            drawerState.open()
        }

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 82.dp)
                ) {
                    MenuLateral(
                        drawerState = drawerState,
                        onCerrarSesion = {
                            scope.launch { drawerState.close() }
                            onCerrarSesion()
                        },
                        onCerrarDrawer = {
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            },
            scrimColor = Color.Transparent
        ) {
            contenidoPrincipal()
        }
    } else {
        contenidoPrincipal()
    }

    // Dialogo de detalles (se abre SOLO cuando se selecciona un producto)
    if (productoSeleccionado != null) {
        ProductoDetalles(
            producto = productoSeleccionado!!,
            onCerrar = { productoSeleccionado = null }
        )
    }
}

@Preview(
    name = "Catalogo Screen Preview",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun CatalogoScreenPreview() {
    AppInventarioTheme {
        CatalogoScreen(mostrarMenuEnPreview = true)
    }
}