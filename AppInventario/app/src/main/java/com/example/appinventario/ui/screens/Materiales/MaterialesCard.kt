package com.example.appinventario.ui.screens.Materiales

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appinventario.data.local.entities.MaterialEntity
import com.example.appinventario.ui.theme.AppColors

@Composable
fun MaterialCard(
    material: MaterialEntity,
    modifier: Modifier = Modifier,
    onEditar: () -> Unit
) {
    // Calcular si el stock está bajo
    val stockBajo = material.stockActual <= material.stockMinimo

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.BrownMid),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(AppColors.BrownLight.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text("IMG", fontSize = 40.sp, color = AppColors.BrownText.copy(alpha = 0.5f))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Nombre
            Text(
                text = material.nombre,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextOnDark,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Stock Actual
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Stock Actual",
                    fontSize = 11.sp,
                    color = AppColors.TextOnCard
                )
                Text(
                    text = material.stockActual.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (stockBajo) AppColors.ErrorRed else AppColors.TextOnDark
                )
            }

            // Stock Mínimo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Stock Mínimo",
                    fontSize = 11.sp,
                    color = AppColors.TextOnCard
                )
                Text(
                    text = material.stockMinimo.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextOnDark
                )
            }

            // Medida
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Medida",
                    fontSize = 11.sp,
                    color = AppColors.TextOnCard
                )
                Text(
                    text = material.unidadMedida,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = AppColors.TextOnDark
                )
            }

            // Precio por unidad
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Precio c/u",
                    fontSize = 11.sp,
                    color = AppColors.TextOnCard
                )
                Text(
                    text = "$${String.format("%.2f", material.precioPorUnidad)}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextOnDark
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón Editar
            Button(
                onClick = onEditar,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.RedDark,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Editar",
                    fontSize = 13.sp,
                    style = androidx.compose.ui.text.TextStyle(
                        color = AppColors.White
                    )
                )
            }
        }
    }
}