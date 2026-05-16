package com.example.appinventario.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appinventario.R
import com.example.appinventario.ui.screens.components.catalogo.ProductoCard
import com.example.appinventario.ui.screens.components.catalogo.BarraBusqueda
import com.example.appinventario.ui.screens.components.catalogo.ProductoDetalles
import com.example.appinventario.ui.theme.AppInventarioTheme

// Simulación de los atributos que serán extraídos de la base de datos
data class Llavero(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double
)

@Composable
fun CatalogoScreen(
    onProductoClick: (Llavero) -> Unit = {},
    mostrarDialogoEnPreview: Boolean = false
) {
    var busquedaTexto by remember { mutableStateOf("") }

    // Estado para el dialogo
    var productoSeleccionado by remember {
        mutableStateOf(
            if (mostrarDialogoEnPreview) {
                Llavero(
                    id = 1,
                    nombre = "Producto Ejemplo",
                    descripcion = "Llavero de cuero personalizado con letras grabadas. Ideal para regalos y detalles especiales. Hecho con cuero 100% genuino.Llavero de cuero personalizado con letras grabadas. Ideal para regalos y detalles especiales. Hecho con cuero 100% genuino.Llavero de cuero personalizado con letras grabadas. Ideal para regalos y detalles especiales. Hecho con cuero 100% genuino.Llavero de cuero personalizado con letras grabadas. Ideal para regalos y detalles especiales. Hecho con cuero 100% genuino.Llavero de cuero personalizado con letras grabadas. Ideal para regalos y detalles especiales. Hecho con cuero 100% genuino.Llavero de cuero personalizado con letras grabadas. Ideal para regalos y detalles especiales. Hecho con cuero 100% genuino.Llavero de cuero personalizado con letras grabadas. Ideal para regalos y detalles especiales. Hecho con cuero 100% genuino.Llavero de cuero personalizado con letras grabadas. Ideal para regalos y detalles especiales. Hecho con cuero 100% genuino.Llavero de cuero personalizado con letras grabadas. Ideal para regalos y detalles especiales. Hecho con cuero 100% genuino.Llavero de cuero personalizado con letras grabadas. Ideal para regalos y detalles especiales. Hecho con cuero 100% genuino.Llavero de cuero personalizado con letras grabadas. Ideal para regalos y detalles especiales. Hecho con cuero 100% genuino.Llavero de cuero personalizado con letras grabadas. Ideal para regalos y detalles especiales. Hecho con cuero 100% genuino.Llavero de cuero personalizado con letras grabadas. Ideal para regalos y detalles especiales. Hecho con cuero 100% genuino.Llavero de cuero personalizado con letras grabadas. Ideal para regalos y detalles especiales. Hecho con cuero 100% genuino.",
                    precio = 100.0
                )
            } else {
                null
            }
        )
    }

    // DATOS SIMULADOS - Lista temporal (cambiar despues por BD)
    val productos = listOf(
        Llavero(1, "Llavero de cuero", "Llavero de cuero personalizado con letras grabadas. Ideal para regalos y detalles especiales. Hecho con cuero 100% genuino.", 50.50),
        Llavero(2, "Llavero metálico", "Llavero de metal grabado con diseño personalizado. Resistente y duradero.", 45.00),
        Llavero(3, "Llavero acrílico", "Llavero acrílico transparente con foto impresa. Perfecto para fotos de mascotas.", 35.50),
        Llavero(4, "Llavero madera", "Llavero artesanal de madera de olivo. Pieza única y ecológica.", 60.00),
        Llavero(5, "Llavero con iniciales", "Llavero de cuero con iniciales grabadas en metal dorado.", 55.00),
        Llavero(6, "Llavero multifuncional", "Llavero con abrebotellas y destapador. Práctico y funcional.", 40.00)
    )

    val productosFiltrados = if (busquedaTexto.isBlank()) {
        productos
    } else {
        productos.filter { producto ->
            producto.nombre.contains(busquedaTexto, ignoreCase = true) ||
                    producto.descripcion.contains(busquedaTexto, ignoreCase = true)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFAED))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen Catálogo
            Image(
                painter = painterResource(id = R.drawable.catalogo_label),
                contentDescription = "Catálogo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Barra de búsqueda
            BarraBusqueda(
                value = busquedaTexto,
                onValueChange = { busquedaTexto = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

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

    // Diálogo de detalles
    if (productoSeleccionado != null) {
        ProductoDetalles(
            producto = productoSeleccionado!!,
            onCerrar = { productoSeleccionado = null }
        )
    }
}

@Preview(
    name = "Catlogo Screen Preview",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun CatalogoScreenPreview() {
    AppInventarioTheme {
        CatalogoScreen(mostrarDialogoEnPreview = false)
    }
}