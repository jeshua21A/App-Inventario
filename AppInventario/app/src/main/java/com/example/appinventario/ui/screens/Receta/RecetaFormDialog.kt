@file:OptIn(ExperimentalMaterial3Api::class)

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
import com.example.appinventario.ui.theme.AppColors

@Composable
fun RecetaFormDialog(
    titulo: String,
    llavero: LlaveroEntity?,
    listaLlaveros: List<LlaveroEntity>,
    listaMateriales: List<MaterialEntity>,
    materialesAsignados: List<Pair<MaterialEntity, Double>>,
    onGuardar: (llaveroId: Int, items: List<Triple<Int, Double, Double>>) -> Unit,
    onCancelar: () -> Unit
) {
    var materialesSeleccionados by remember {
        mutableStateOf(materialesAsignados.toMutableList())
    }
    var materialSeleccionado by remember { mutableStateOf<MaterialEntity?>(null) }
    var cantidadNueva by remember { mutableStateOf("") }
    var expandedMaterial by remember { mutableStateOf(false) }

    var llaveroSeleccionado by remember { mutableStateOf(llavero) }
    var errorLlavero by remember { mutableStateOf<String?>(null) }

    val materialesDisponibles = remember(listaMateriales, materialesSeleccionados) {
        listaMateriales.filter { material ->
            materialesSeleccionados.none { it.first.id == material.id }
        }
    }

    Dialog(
        onDismissRequest = onCancelar,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .heightIn(max = 700.dp),
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
                Text(
                    text = titulo,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.BrownText
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Selector o info del llavero
                if (llavero == null) {
                    LlaveroSelector(
                        llaveroSeleccionado = llaveroSeleccionado,
                        listaLlaveros = listaLlaveros,
                        onLlaveroSelected = {
                            llaveroSeleccionado = it
                            errorLlavero = null
                        },
                        isError = errorLlavero != null,
                        errorMessage = errorLlavero
                    )
                } else {
                    LlaveroInfoCard(llavero = llavero)
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = AppColors.BrownLight.copy(alpha = 0.3f))
                Spacer(modifier = Modifier.height(16.dp))

                // Seccion de agregar materiales
                Text(
                    text = "Agregar material:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = AppColors.BrownText,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        ExposedDropdownMenuBox(
                            expanded = expandedMaterial,
                            onExpandedChange = { expandedMaterial = it }
                        ) {
                            OutlinedTextField(
                                value = materialSeleccionado?.nombre ?: "",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Seleccionar material") },
                                placeholder = { Text("Elige un material") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMaterial) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AppColors.BrownLight,
                                    unfocusedBorderColor = AppColors.BrownLight.copy(alpha = 0.5f)
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = expandedMaterial,
                                onDismissRequest = { expandedMaterial = false },
                                modifier = Modifier.heightIn(max = 300.dp)
                            ) {
                                if (materialesDisponibles.isEmpty()) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                "No hay materiales disponibles",
                                                color = AppColors.BrownSub
                                            )
                                        },
                                        onClick = { expandedMaterial = false }
                                    )
                                } else {
                                    materialesDisponibles.forEach { material ->
                                        DropdownMenuItem(
                                            text = {
                                                Column {
                                                    Text(
                                                        text = material.nombre,
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.Medium,
                                                        color = AppColors.BrownText
                                                    )
                                                    Text(
                                                        text = "Stock: ${material.stockActual} ${material.unidadMedida}",
                                                        fontSize = 11.sp,
                                                        color = AppColors.BrownSub
                                                    )
                                                }
                                            },
                                            onClick = {
                                                materialSeleccionado = material
                                                expandedMaterial = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = cantidadNueva,
                        onValueChange = { cantidadNueva = it },
                        label = { Text("Cantidad") },
                        modifier = Modifier.width(100.dp),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AppColors.BrownLight,
                            unfocusedBorderColor = AppColors.BrownLight.copy(alpha = 0.5f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val material = materialSeleccionado
                        val cantidad = cantidadNueva.toDoubleOrNull()
                        if (material != null && cantidad != null && cantidad > 0) {
                            materialesSeleccionados.add(material to cantidad)
                            materialSeleccionado = null
                            cantidadNueva = ""
                        }
                    },
                    enabled = materialSeleccionado != null && cantidadNueva.isNotBlank(),
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
                } else {
                    Text(
                        text = "No hay materiales asignados",
                        fontSize = 13.sp,
                        color = AppColors.BrownSub,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
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

                    Button(
                        onClick = {
                            val llaveroId = llaveroSeleccionado?.id ?: llavero?.id
                            if (llaveroId == null) {
                                errorLlavero = "Debes seleccionar un llavero"
                                return@Button
                            }
                            if (materialesSeleccionados.isEmpty()) {
                                return@Button
                            }
                            val items = materialesSeleccionados.map { (material, cantidad) ->
                                Triple(material.id, cantidad, material.precioPorUnidad)
                            }
                            onGuardar(llaveroId, items)
                            onCancelar()
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