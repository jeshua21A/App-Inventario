package com.example.appinventario.ui.screens.Edicion

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
import com.example.appinventario.ui.theme.AppColors

@Composable
fun ProductoCardEditable(
    producto: LlaveroEntity,
    modifier: Modifier = Modifier,
    onEditar: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.BrownMid),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(AppColors.BrownLight.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text("IMG", fontSize = 48.sp, color = AppColors.BrownText.copy(alpha = 0.5f))
            }
            Spacer(Modifier.height(8.dp))
            Text(producto.nombre, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AppColors.TextOnDark, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(4.dp))
            Text(if (producto.descripcion.length > 60) producto.descripcion.take(60) + "..." else producto.descripcion, fontSize = 14.sp, color = AppColors.TextOnCard, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(8.dp))
            Text("$${String.format("%.2f", producto.precioVenta)}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = AppColors.TextOnDark)
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onEditar,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.RedDark, contentColor = AppColors.TextOnDark)
            ) {
                Text("Editar", fontSize = 13.sp)
            }
        }
    }
}