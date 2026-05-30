package com.example.appinventario.ui.screens.Receta

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.appinventario.data.local.entities.LlaveroEntity
import com.example.appinventario.data.local.entities.MaterialEntity
import com.example.appinventario.ui.screens.Receta.MaterialAsignadoCard
import com.example.appinventario.ui.theme.AppColors

@Composable
fun RecetaFormDialog(
    titulo: String,
    llavero: LlaveroEntity?,
    listaMateriales: List<MaterialEntity>,
    materialesAsignados: List<Pair<MaterialEntity, Double>>,
    onGuardar: (llaveroId: Int, items: List<Triple<Int, Double, Double>>) -> Unit,
    onCancelar: () -> Unit
) {
    var materialesSeleccionados by remember {
        mutableStateOf(materialesAsignados.toMutableList())
    }
    var materialSeleccionadoId by remember { mutableStateOf<Int?>(null) }
    var cantidadNueva by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onCancelar,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .heightIn(max = 650.dp),
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

                Spacer(modifier = Modifier.height(8.dp))

                // Informacion del llavero
                if (llavero != null) {
                    Text(
                        text = "Producto: ${llavero.nombre}",
                        fontSize = 14.sp,
                        color = AppColors.BrownSub
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Selector de material
                Text(
                    text = "Agregar material:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = AppColors.BrownText,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Fila con Seleccionar material (izquierda) y Cantidad (derecha)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Seleccionar material
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        // TODO: Reemplazar con ExposedDropdownMenuBox cuando se conecte al ViewModel
                        OutlinedTextField(
                            value = materialSeleccionadoId?.toString() ?: "",
                            onValueChange = {},
                            label = { Text("Seleccionar material") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    // Cantidad
                    OutlinedTextField(
                        value = cantidadNueva,
                        onValueChange = { cantidadNueva = it },
                        label = { Text("Cantidad") },
                        modifier = Modifier.width(100.dp),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                }

                // Boton Agregar (centrado debajo)
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val material = listaMateriales.find { it.id == materialSeleccionadoId }
                        val cantidad = cantidadNueva.toDoubleOrNull()
                        if (material != null && cantidad != null && cantidad > 0) {
                            if (materialesSeleccionados.none { it.first.id == material.id }) {
                                materialesSeleccionados.add(material to cantidad)
                            }
                            materialSeleccionadoId = null
                            cantidadNueva = ""
                        }
                    },
                    enabled = materialSeleccionadoId != null && cantidadNueva.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.BrownLight,
                        contentColor = AppColors.TextOnDark
                    )
                ) {
                    Text("Agregar", fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Lista de materiales asignados
                if (materialesSeleccionados.isNotEmpty()) {
                    Text(
                        text = "Materiales asignados:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = AppColors.BrownText,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier.heightIn(max = 200.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(materialesSeleccionados) { (material, cantidad) ->
                            MaterialAsignadoCard(
                                material = material,
                                cantidad = cantidad,
                                onCantidadCambio = { nuevaCantidad ->
                                    val index = materialesSeleccionados.indexOfFirst { it.first.id == material.id }
                                    if (index != -1) {
                                        materialesSeleccionados[index] = material to nuevaCantidad
                                    }
                                },
                                onEliminar = {
                                    materialesSeleccionados.removeAll { it.first.id == material.id }
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botones Cancelar y Guardar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onCancelar,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.RedLigh,
                            contentColor = AppColors.Cream
                        )
                    ) {
                        Text("Cancelar", fontSize = 14.sp)
                    }

                    Button(
                        onClick = {
                            if (llavero != null && materialesSeleccionados.isNotEmpty()) {
                                val items = materialesSeleccionados.map { (material, cantidad) ->
                                    Triple(material.id, cantidad, material.precioPorUnidad)
                                }
                                onGuardar(llavero.id, items)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.RedDark,
                            contentColor = AppColors.Cream
                        )
                    ) {
                        Text("Guardar", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}