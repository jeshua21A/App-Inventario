package com.example.appinventario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.appinventario.navigation.AppNavGraph
import com.example.appinventario.ui.theme.AppInventarioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppInventarioTheme {
                // El NavGraph ahora centraliza toda la lógica de pantallas
                AppNavGraph()
            }
        }
    }
}