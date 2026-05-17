package com.example.appinventario.ui.screens.components.catalogo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuLateral(
    drawerState: DrawerState,
    onCerrarSesion: () -> Unit,
    onCerrarDrawer: () -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .width(200.dp)
            .background(Color(0xFFb23e3e))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onCerrarSesion,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0x00FFFFFF),
                contentColor = Color(0xfffffaed)
            )
        ) {
            Text("Cerrar sesion", fontSize = 20.sp)
        }
    }
}