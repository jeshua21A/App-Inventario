package com.example.appinventario.ui.screens.Edicion

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.appinventario.ui.theme.AppColors

@Composable
fun LlaveroFormDialog(
    titulo: String,
    nombreInicial: String = "",
    descInicial: String = "",
    precioInicial: String = "",
    onGuardar: (nombre: String, descripcion: String, precio: Double) -> Unit,
    onEliminar: (() -> Unit)? = null,
    onCancelar: () -> Unit
) {
    var nombre by remember { mutableStateOf(nombreInicial) }
    var desc by remember { mutableStateOf(descInicial) }
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
                .heightIn(max = 600.dp),  // ← Altura máxima, pero no fija
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = AppColors.Cream),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            // Agregar scroll a toda la columna
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),  // ← Scroll habilitado
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título
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
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppColors.BrownLight,
                        unfocusedBorderColor = AppColors.BrownLight.copy(alpha = 0.5f)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Área de foto
                Text(
                    text = "Foto del producto",
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
                            modifier = Modifier.size(48.dp),
                            tint = AppColors.BrownLight
                        )
                        Text(
                            text = "Vista previa de la foto",
                            fontSize = 12.sp,
                            color = AppColors.BrownSub
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón Cambiar foto
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

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Descripción
                OutlinedTextField(
                    value = desc,
                    onValueChange = { if (it.length <= 255) desc = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    shape = RoundedCornerShape(12.dp),
                    supportingText = {
                        Text(
                            "${desc.length}/255",
                            fontSize = 10.sp,
                            color = if (desc.length > 250) AppColors.ErrorRed else AppColors.BrownSub
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppColors.BrownLight,
                        unfocusedBorderColor = AppColors.BrownLight.copy(alpha = 0.5f)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Precio
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it; errorMsg = null },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    prefix = { Text("$", color = AppColors.BrownSub) },
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppColors.BrownLight,
                        unfocusedBorderColor = AppColors.BrownLight.copy(alpha = 0.5f)
                    )
                )

                // Mensaje de error
                if (errorMsg != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMsg!!,
                        color = AppColors.ErrorRed,
                        fontSize = 12.sp
                    )
                }

                // Botón Eliminar (si está en modo edición)
                if (onEliminar != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(
                        onClick = onEliminar,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Eliminar producto", color = AppColors.ErrorRed, fontSize = 14.sp)
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

                    // Botón Aceptar
                    Button(
                        onClick = {
                            val precioDouble = precio.toDoubleOrNull()
                            when {
                                nombre.isBlank() -> errorMsg = "El nombre es obligatorio"
                                precioDouble == null || precioDouble <= 0 -> errorMsg = "Precio inválido"
                                else -> {
                                    onGuardar(nombre.trim(), desc.trim(), precioDouble)
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