package com.example.appinventario.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appinventario.R
import com.example.appinventario.ui.theme.AppInventarioTheme

// Simulación de los atributos que serán extraídos de la base de datos
data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double
)

@Composable
fun CatalogoScreen(
    onProductoClick: (Producto) -> Unit = {}
) {
    var busquedaTexto by remember { mutableStateOf("") }

    val productos = listOf(
        Producto(1, "Llavero de cuero", "Llavero de cuero personalizado con letras grabadas", 50.50),
        Producto(2, "Llavero de cuero", "Llavero de cuero personalizado con letras grabadas", 50.50),
        Producto(3, "Llavero de cuero", "Personalizado con letras grabadas", 50.50),
        Producto(4, "Llavero de cuero", "Llavero de cuero personalizado con letras grabadas", 50.50),
        Producto(5, "Llavero de cuero", "Llavero de cuero personalizado con letras grabadas", 50.50),
        Producto(6, "Llavero de cuero", "Llavero de cuero personalizado con letras grabadas", 50.50)
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

            // Cuadro de busqueda
            OutlinedTextField(
                value = busquedaTexto,
                onValueChange = { busquedaTexto = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                placeholder = {
                    Text(
                        text = "Buscar productos...",
                        color = Color.Gray
                    )
                },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(0xFFD4A373),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                singleLine = true
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
                            ProductoCardVertical(
                                producto = filaProductos[0],
                                onClick = { onProductoClick(filaProductos[0]) },
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        if (filaProductos.size > 1) {
                            ProductoCardVertical(
                                producto = filaProductos[1],
                                onClick = { onProductoClick(filaProductos[1]) },
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

@Composable
fun ProductoCardVertical(
    producto: Producto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFCC845B)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Espacio para la imagen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFD4A373).copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                //Campo de la imagen (Cambiar mas adelante por iumagen en lugar de texto)
                Text(
                    text = "Img",
                    fontSize = 48.sp,
                    color = Color(0xFF5D4037)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            //Nombre del producto
            Text(
                text = producto.nombre,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            //Descripcion del producto
            Text(
                text = producto.descripcion,
                fontSize = 14.sp,
                color = Color(0xFFFFF5E6),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            //Precio del Producto
            Text(
                text = "$${String.format("%.2f", producto.precio)}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFfffaed)
            )
        }
    }
}

@Preview(
    name = "Catálogo Screen Preview",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun CatalogoScreenPreview() {
    AppInventarioTheme {
        CatalogoScreen()
    }
}