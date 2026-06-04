package com.example.appinventario.ui.screens.Receta

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appinventario.data.local.entities.LlaveroEntity
import com.example.appinventario.ui.theme.AppColors

@Composable
fun LlaveroInfoCard(
    llavero: LlaveroEntity,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.BrownLight.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Llavero:",
                    fontSize = 12.sp,
                    color = AppColors.BrownSub
                )
                Text(
                    text = llavero.nombre,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.BrownText
                )
                Text(
                    text = llavero.descripcion.take(50),
                    fontSize = 11.sp,
                    color = AppColors.BrownSub
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Precio:",
                    fontSize = 12.sp,
                    color = AppColors.BrownSub
                )
                Text(
                    text = "$${String.format("%.2f", llavero.precioVenta)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.RedDark
                )
            }
        }
    }
}