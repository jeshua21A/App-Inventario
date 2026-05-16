package com.example.appinventario.ui.screens.components.catalogo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appinventario.ui.screens.Llavero

// DATOS SIMULADOS - No hay acceso a BD aún

@Composable
fun ProductoCard(
    producto: Llavero,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFDC8959)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Espacio para la imagen (simulado)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFD4A373).copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "IMG",
                    fontSize = 48.sp,
                    color = Color(0xFF80563A)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre del producto
            Text(
                text = producto.nombre,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Descripción simulada
            Text(
                text = if (producto.descripcion.length > 60) producto.descripcion.take(60) + "..." else producto.descripcion,
                fontSize = 14.sp,
                color = Color(0xFFFFF5E6),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Precio del Producto
            Text(
                text = "$${String.format("%.2f", producto.precio)}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFfffaed)
            )
        }
    }
}