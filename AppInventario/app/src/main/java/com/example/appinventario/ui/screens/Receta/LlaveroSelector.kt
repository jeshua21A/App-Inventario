package com.example.appinventario.ui.screens.Receta

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appinventario.data.local.entities.LlaveroEntity
import com.example.appinventario.ui.theme.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LlaveroSelector(
    llaveroSeleccionado: LlaveroEntity?,
    listaLlaveros: List<LlaveroEntity>,
    onLlaveroSelected: (LlaveroEntity) -> Unit,
    isError: Boolean = false,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = "Seleccionar Llavero:",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = AppColors.BrownText
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = llaveroSeleccionado?.nombre ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Llavero") },
                placeholder = { Text("Selecciona un llavero") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                isError = isError,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppColors.BrownLight,
                    unfocusedBorderColor = AppColors.BrownLight.copy(alpha = 0.5f),
                    errorBorderColor = AppColors.ErrorRed
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.heightIn(max = 300.dp)
            ) {
                if (listaLlaveros.isEmpty()) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                "No hay llaveros disponibles",
                                color = AppColors.BrownSub
                            )
                        },
                        onClick = { expanded = false }
                    )
                } else {
                    listaLlaveros.forEach { llavero ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(
                                        text = llavero.nombre,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = AppColors.BrownText
                                    )
                                    Text(
                                        text = "$${String.format("%.2f", llavero.precioVenta)}",
                                        fontSize = 11.sp,
                                        color = AppColors.BrownSub
                                    )
                                }
                            },
                            onClick = {
                                onLlaveroSelected(llavero)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = AppColors.ErrorRed,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}