package com.example.appinventario.ui.screens.Receta

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appinventario.data.local.entities.LlaveroEntity
import com.example.appinventario.data.local.entities.MaterialEntity
import com.example.appinventario.ui.theme.AppColors

@Composable
fun RecetaCard(
    llavero: LlaveroEntity,
    materiales: List<Pair<MaterialEntity, Double>>, // (material, cantidad)
    onEditar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.BrownMid),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header: Nombre del llavero y precio
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = llavero.nombre,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AppColors.TextOnDark,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "$${String.format("%.2f", llavero.precioVenta)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextOnDark
                )
            }

            // Descripcion
            Text(
                text = llavero.descripcion,
                fontSize = 17.sp,
                color = AppColors.TextOnCard,
                maxLines = 2,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis
            )

            HorizontalDivider(
                color = AppColors.TextOnCard.copy(alpha = 0.3f),
                thickness = 1.dp
            )

            // Materiales necesarios
            Text(
                text = "Materiales necesarios:",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AppColors.TextOnDark
            )

            if (materiales.isEmpty()) {
                Text(
                    text = "Sin materiales asignados",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextOnCard.copy(alpha = 0.7f)
                )
            } else {
                materiales.forEach { (material, cantidad) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = material.nombre,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.TextOnCard
                        )
                        Text(
                            text = "${cantidad} ${material.unidadMedida}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AppColors.TextOnCard
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Boton Editar
            Button(
                onClick = onEditar,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.RedDark,
                    contentColor = AppColors.TextOnDark
                )
            ) {
                Text("Editar Receta", fontSize = 13.sp)
            }
        }
    }
}