package com.example.appinventario.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.appinventario.data.local.entities.LlaveroEntity
import com.example.appinventario.ui.theme.AppColors

@Composable
fun ProductoDetallesDialog(
    llavero: LlaveroEntity,     
    onCerrar: () -> Unit
) {
    // Capa gris transparente de fondo
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onCerrar() }
    ) {
        Dialog(
            onDismissRequest = onCerrar,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .height(660.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.Cream
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .padding(vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // TODO: Reemplazar con imagen real cuando esté disponible en la base de datos
                    // La entidad LlaveroEntity no tiene campo de imagen por ahora
                    Box(
                        modifier = Modifier
                            .size(300.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(AppColors.BrownLight.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "IMG", fontSize = 40.sp, color = AppColors.BrownText.copy(alpha = 0.5f))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Nombre
                    Text(
                        text = llavero.nombre,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.BrownText,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Precio
                    Text(
                        text = "$${String.format("%.2f", llavero.precioVenta)}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.BrownLight
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    HorizontalDivider(
                        color = AppColors.BrownLight.copy(alpha = 0.3f),
                        thickness = 1.dp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Descripción
                    Text(
                        text = "Descripción:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.BrownText,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Área de scroll para descripciones largas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = llavero.descripcion,
                            fontSize = 13.sp,
                            color = AppColors.BrownSub,
                            lineHeight = 20.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Botón Cerrar
                    Row(
                        modifier = Modifier.width(120.dp),
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        OutlinedButton(
                            onClick = onCerrar,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = AppColors.BrownText
                            )
                        ) {
                            Text("Cerrar")
                        }
                    }
                }
            }
        }
    }
}