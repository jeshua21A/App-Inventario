package com.example.appinventario.ui.screens.LoginScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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

    // Estados para mensajes de error
    var errorUsuario by remember { mutableStateOf<String?>(null) }
    var errorContrasena by remember { mutableStateOf<String?>(null) }
    var errorGeneral by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFAED))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .padding(top = 65.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.login_logo),
                contentDescription = "Logo de la aplicación",
                modifier = Modifier.size(300.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Campos de login
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Campo Usuario con mensaje de error
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = usuario,
                        onValueChange = {
                            usuario = it
                            errorUsuario = null // Limpiar error al escribir
                            errorGeneral = null
                        },
                        label = { Text("Usuario") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = errorUsuario != null,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            // Para el texto escrito por el usuario
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,

                            // Para el label
                            unfocusedLabelColor = Color.White,
                            // El fondo del campo
                            focusedContainerColor = Color(0xFFcf6060),
                            unfocusedContainerColor = Color(0xFFb23e3e),
                            // Colores para estado de error
                            errorBorderColor = Color.Red,
                            focusedBorderColor = if (errorUsuario != null) Color.Red else Color(0xFF9F6C57),
                            unfocusedBorderColor = if (errorUsuario != null) Color.Red else Color.Gray
                        )
                    )

                    // Mensaje de error para Usuario (solo visible cuando hay error)
                    if (errorUsuario != null) {
                        Text(
                            text = errorUsuario!!,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Contraseña con mensaje de error
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = contrasena,
                        onValueChange = {
                            contrasena = it
                            errorContrasena = null
                            errorGeneral = null
                        },
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        isError = errorContrasena != null,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF9F6C57),
                            unfocusedBorderColor = if (errorContrasena != null) Color.Red else Color.Gray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            unfocusedLabelColor = Color.White,
                            focusedContainerColor = Color(0xFFcf6060),
                            unfocusedContainerColor = Color(0xFFb23e3e),
                            errorBorderColor = Color.Red
                        )
                    )

                    // Mensaje de error para Contraseña (solo visible cuando hay error)
                    if (errorContrasena != null) {
                        Text(
                            text = errorContrasena!!,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Mensaje de error general (solo visible cuando hay error)
                if (errorGeneral != null) {
                    Text(
                        text = errorGeneral!!,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón Iniciar Sesión
                Button(
                    onClick = {
                        // Limpiar errores previos
                        errorUsuario = null
                        errorContrasena = null
                        errorGeneral = null

                        // Validaciones simuladas
                        when {
                            usuario.isBlank() -> {
                                errorUsuario = "El usuario es obligatorio"
                            }
                            contrasena.isBlank() -> {
                                errorContrasena = "La contraseña es obligatoria"
                            }
                            else -> {
                                // Simular validación de credenciales
                                if (usuario == "admin" && contrasena == "1234") {
                                    onLoginExitoso()
                                } else {
                                    errorGeneral = "Usuario o contraseña incorrectos"
                                }
                            }
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
