package com.example.appinventario.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appinventario.R
import com.example.appinventario.ui.theme.AppColors
import com.example.appinventario.ui.theme.AppInventarioTheme

@Composable
fun LoginScreen(
    //TODO: Cambiar parametro para que reciba el viewmodel
    onLoginExitoso: () -> Unit = {}
) {
    //- Dejar usuario y contraseña
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    //TODO: quitar error general y reemplazarlo por otros parametros de viewmodel
    //TODO: Incluir authState
    var errorGeneral by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Cream)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .statusBarsPadding()
                .padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.login_logo),
                contentDescription = "Logo de la aplicación",
                modifier = Modifier.size(280.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campo Usuario
            OutlinedTextField(
                value = usuario,
                onValueChange = {
                    usuario = it
                    errorGeneral = null
                },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorGeneral != null,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = AppColors.BrownMid.copy(alpha = 0.8f),
                    unfocusedContainerColor = AppColors.RedDark.copy(alpha = 0.7f),
                    focusedTextColor = AppColors.TextOnDark,
                    unfocusedTextColor = AppColors.TextOnDark,
                    unfocusedLabelColor = AppColors.TextOnDark,
                    focusedLabelColor = AppColors.TextOnDark,
                    focusedBorderColor = AppColors.BrownLight,
                    unfocusedBorderColor = AppColors.BrownLight.copy(alpha = 0.3f),
                    errorBorderColor = AppColors.ErrorRed
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Contraseña
            OutlinedTextField(
                value = contrasena,
                onValueChange = {
                    contrasena = it
                    //TODO: Error generico, cambiar
                    errorGeneral = null
                },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                //TODO: Error generico, cambiar
                isError = errorGeneral != null,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = AppColors.BrownMid.copy(alpha = 0.8f),
                    unfocusedContainerColor = AppColors.RedDark.copy(alpha = 0.7f),
                    focusedTextColor = AppColors.TextOnDark,
                    unfocusedTextColor = AppColors.TextOnDark,
                    unfocusedLabelColor = AppColors.TextOnDark,
                    focusedLabelColor = AppColors.TextOnDark,
                    focusedBorderColor = AppColors.BrownLight,
                    unfocusedBorderColor = AppColors.BrownLight.copy(alpha = 0.3f),
                    errorBorderColor = AppColors.ErrorRed
                )
            )

            // Mensaje de error
            //TODO: error generico, cambiar
            if (errorGeneral != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorGeneral!!,
                    color = AppColors.ErrorRed,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón Iniciar Sesion
            Button(
                //-Logica de Boton solo de prueba, reemplazarlo por funcion del viewmodel
                onClick = {
                    when {
                        //TODO: error generico, cambiar
                        usuario.isBlank() -> errorGeneral = "El usuario es obligatorio"
                        contrasena.isBlank() -> errorGeneral = "La contraseña es obligatoria"
                        else -> {
                            if (usuario == "admin" && contrasena == "1234") {
                                onLoginExitoso()
                            } else {
                                //TODO: Error generico, cambiar
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
                    containerColor = AppColors.BrownLight,
                    contentColor = AppColors.TextOnDark
                )
            ) {
                Text("Iniciar Sesión", fontSize = 16.sp)
            }
        }
    }
}

// Preview
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