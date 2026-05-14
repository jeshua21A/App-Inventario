package com.example.appinventario.ui.screens.LoginScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appinventario.R
import com.example.appinventario.ui.theme.AppInventarioTheme

@Composable
fun LoginScreen(
    onLoginExitoso: () -> Unit = {}
) {
    // Estados para los campos de texto
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFAED))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.login_logo),
                contentDescription = "Logo de la aplicación",
                modifier = Modifier.size(300.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campos de login - SIN recuadro blanco
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Campo Usuario
                OutlinedTextField(
                    value = usuario,
                    onValueChange = { usuario = it },
                    label = { Text("Usuario") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        // Para el texto ESCRITO por el usuario
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,

                        // Para el label
                        unfocusedLabelColor = Color.White,
                        // El fondo del campo 
                        focusedContainerColor = Color(0xFFcf6060),
                        unfocusedContainerColor = Color(0xFFb23e3e)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Contraseña
                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF9F6C57),
                        unfocusedBorderColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        focusedContainerColor = Color(0xFFcf6060),
                        unfocusedContainerColor = Color(0xFFb23e3e)
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Botón Iniciar Sesión
                Button(
                    onClick = {
                        if (usuario.isNotBlank() && contrasena.isNotBlank()) {
                            onLoginExitoso()
                        }
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF6C19F),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Iniciar Sesión",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

// Vista previa
@Preview(
    name = "Login Screen Preview",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LoginScreenPreview() {
    AppInventarioTheme {
        LoginScreen()
    }
}
