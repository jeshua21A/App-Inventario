package com.example.appinventario.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appinventario.ui.viewmodels.InventarioViewModel

@Composable
fun MaterialesScreenPrueba(viewModel: InventarioViewModel){
    // observamos la lista de materiales del ViewModel
    val listaMateriales by viewModel.listaMateriales.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding()
    ) {
        Text(text = "Registro de Materiales", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // --- Formulario ---
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del material") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Cantidad") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio Costo") },
                modifier = Modifier.weight(1f)
            )
        }

        Button(
            onClick = {
                if (nombre.isNotBlank() && stock.isNotBlank()) {
                    viewModel.agregarMaterial(
                        nombre = nombre,
                        stock = stock.toDoubleOrNull() ?: 0.0,
                        unidad = "pza",
                        minimo = 1.0,
                        precio = precio.toDoubleOrNull() ?: 0.0
                    )
                    // Limpiar campos despues de guardar
                    nombre = ""
                    stock = ""
                    precio = ""
                }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        ) {
            Text("Guardando datos en el inventario")
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        // --- Lista de Materiales ---
        Text(
            text = "Inventario Actual",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(listaMateriales) { material ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    ListItem(
                        headlineContent = { Text(material.nombre) },
                        supportingContent = { Text("Stock: ${material.stockActual} | Costo: ${material.precioPorUnidad}") },
                        trailingContent = { Text("ID: ${material.id}") }
                    )
                }
            }
        }
    }

}