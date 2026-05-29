package com.example.appinventario.ui.screens.Materiales

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.appinventario.ui.theme.AppColors

@Composable
fun MaterialFormDialog(
    titulo: String,
    nombreInicial: String = "",
    stockActualInicial: String = "",
    stockMinimoInicial: String = "",
    unidadMedidaInicial: String = "",
    precioInicial: String = "",
    onGuardar: (
        nombre: String,
        stockActual: Double,
        stockMinimo: Double,
        unidadMedida: String,
        precio: Double
    ) -> Unit,
    onEliminar: (() -> Unit)? = null,
    onCancelar: () -> Unit
) {
    var nombre by remember { mutableStateOf(nombreInicial) }
    var stockActual by remember { mutableStateOf(stockActualInicial) }
    var stockMinimo by remember { mutableStateOf(stockMinimoInicial) }
    var unidadMedida by remember { mutableStateOf(unidadMedidaInicial) }
    var precio by remember { mutableStateOf(precioInicial) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onCancelar,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .heightIn(max = 600.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = AppColors.Cream),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Titulo
                Text(
                    text = titulo,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.BrownText
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Campo Nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it; errorMsg = null },
                    label = { Text("Nombre del material") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppColors.BrownLight,
                        unfocusedBorderColor = AppColors.BrownLight.copy(alpha = 0.5f)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Stock Actual
                OutlinedTextField(
                    value = stockActual,
                    onValueChange = { stockActual = it; errorMsg = null },
                    label = { Text("Stock Actual") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppColors.BrownLight,
                        unfocusedBorderColor = AppColors.BrownLight.copy(alpha = 0.5f)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo Stock Mínimo
                OutlinedTextField(
                    value = stockMinimo,
                    onValueChange = { stockMinimo = it; errorMsg = null },
                    label = { Text("Stock Mínimo") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppColors.BrownLight,
                        unfocusedBorderColor = AppColors.BrownLight.copy(alpha = 0.5f)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo Unidad de Medida
                OutlinedTextField(
                    value = unidadMedida,
                    onValueChange = { unidadMedida = it; errorMsg = null },
                    label = { Text("Unidad de Medida") },
                    placeholder = { Text("Ej: kg, pza, m, cm, ml") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppColors.BrownLight,
                        unfocusedBorderColor = AppColors.BrownLight.copy(alpha = 0.5f)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo Precio por Unidad
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it; errorMsg = null },
                    label = { Text("Precio por unidad") },
                    modifier = Modifier.fillMaxWidth(),
                    prefix = { Text("$", color = AppColors.BrownSub) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppColors.BrownLight,
                        unfocusedBorderColor = AppColors.BrownLight.copy(alpha = 0.5f)
                    )
                )

                // Area de foto (opcional para materiales)
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Foto del material (opcional)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = AppColors.BrownText,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(AppColors.BrownLight.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Foto",
                            modifier = Modifier.size(40.dp),
                            tint = AppColors.BrownLight
                        )
                        Text(
                            text = "Vista previa de la foto",
                            fontSize = 10.sp,
                            color = AppColors.BrownSub
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Boton Cambiar foto
                OutlinedButton(
                    onClick = { /* TODO: Implementar selector de imagen */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = AppColors.BrownLight
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Cambiar foto",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cambiar foto", fontSize = 14.sp)
                }

                // Mensaje de error
                if (errorMsg != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMsg!!,
                        color = AppColors.ErrorRed,
                        fontSize = 12.sp
                    )
                }

                // Boton Eliminar (si esta en modo edicion)
                if (onEliminar != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(
                        onClick = onEliminar,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Eliminar material", color = AppColors.ErrorRed, fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botones Aceptar y Cancelar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Botón Cancelar
                    OutlinedButton(
                        onClick = onCancelar,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = AppColors.BrownText
                        )
                    ) {
                        Text("Cancelar", fontSize = 14.sp)
                    }

                    // Boton Aceptar
                    Button(
                        onClick = {
                            val stockActualDouble = stockActual.toDoubleOrNull()
                            val stockMinimoDouble = stockMinimo.toDoubleOrNull()
                            val precioDouble = precio.toDoubleOrNull()

                            when {
                                nombre.isBlank() -> errorMsg = "El nombre es obligatorio"
                                stockActualDouble == null || stockActualDouble < 0 -> errorMsg = "Stock actual inválido"
                                stockMinimoDouble == null || stockMinimoDouble < 0 -> errorMsg = "Stock mínimo inválido"
                                unidadMedida.isBlank() -> errorMsg = "La unidad de medida es obligatoria"
                                precioDouble == null || precioDouble < 0 -> errorMsg = "Precio inválido"
                                else -> {
                                    onGuardar(
                                        nombre.trim(),
                                        stockActualDouble,
                                        stockMinimoDouble,
                                        unidadMedida.trim(),
                                        precioDouble
                                    )
                                    onCancelar()
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.RedDark,
                            contentColor = AppColors.Cream
                        )
                    ) {
                        Text("Aceptar", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}