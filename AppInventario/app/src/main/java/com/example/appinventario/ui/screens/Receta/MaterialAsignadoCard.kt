package com.example.appinventario.ui.screens.Receta

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appinventario.data.local.entities.MaterialEntity
import com.example.appinventario.ui.theme.AppColors

@Composable
fun MaterialAsignadoCard(
    material: MaterialEntity,
    cantidad: Double,
    onCantidadCambio: (Double) -> Unit,
    onEliminar: () -> Unit
) {
    var cantidadTexto by remember(cantidad) { mutableStateOf(cantidad.toString()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = AppColors.BrownLight.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = material.nombre,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.BrownText
                )
                Text(
                    text = "Stock: ${material.stockActual} ${material.unidadMedida}",
                    fontSize = 11.sp,
                    color = AppColors.BrownSub
                )
            }

            OutlinedTextField(
                value = cantidadTexto,
                onValueChange = { nuevaCantidad ->
                    cantidadTexto = nuevaCantidad
                    val cantidadDouble = nuevaCantidad.toDoubleOrNull()
                    if (cantidadDouble != null && cantidadDouble > 0) {
                        onCantidadCambio(cantidadDouble)
                    }
                },
                modifier = Modifier.width(90.dp),
                singleLine = true,
                shape = MaterialTheme.shapes.small,
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 14.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppColors.BrownLight,
                    unfocusedBorderColor = AppColors.BrownLight.copy(alpha = 0.5f)
                )
            )

            IconButton(onClick = onEliminar) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = AppColors.ErrorRed
                )
            }
        }
    }
}