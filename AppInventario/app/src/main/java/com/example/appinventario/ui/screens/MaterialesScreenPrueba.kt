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
import androidx.compose.ui.graphics.Color  // ← AGREGADO
import androidx.compose.foundation.rememberScrollState  // ← AGREGADO
import androidx.compose.foundation.verticalScroll  // ← AGREGADO
import androidx.compose.ui.text.font.FontWeight  // ← AGREGADO
import androidx.compose.ui.unit.sp  // ← AGREGADO
import com.example.appinventario.ui.viewmodels.InventarioViewModel

@Composable
fun MaterialesScreenPrueba(viewModel: InventarioViewModel){
    // observamos la lista de materiales del ViewModel
    val listaMateriales by viewModel.listaMateriales.collectAsState()

    //Estados para Supabase
    val syncMessage by viewModel.syncMessage.collectAsState()
    val isSyncing by viewModel.isSyncing.collectAsState()

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

        //Seccion de prueba de Supabase
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = "🔌 Conexión con Supabase",
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.testSupabaseConnection() },
                        modifier = Modifier.weight(1f),
                        enabled = !isSyncing
                    ) {
                        Text(if (isSyncing) "Probando..." else "📡 Probar Conexión")
                    }

                    Button(
                        onClick = { viewModel.syncMaterialesFromCloud() },
                        modifier = Modifier.weight(1f),
                        enabled = !isSyncing,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        )
                    ) {
                        Text(if (isSyncing) "Sincronizando..." else "🔄 Sincronizar desde Nube")
                    }
                }

                // Mostrar mensaje de estado de sincronizacion
                if (syncMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = syncMessage!!,
                        fontSize = 12.sp,
                        color = when {
                            syncMessage!!.contains("sincronizado") -> Color(0xFF2E7D32)
                            syncMessage!!.contains("no sincronizado") -> Color(0xFFC62828)
                            else -> Color(0xFFE65100)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Formulario ---
        Text("Agregar nuevo material", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

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
                    // Limpiar campos después de guardar
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
            text = "Inventario Actual (${listaMateriales.size} materiales)",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        if (listaMateriales.isEmpty()) {
            Text(
                text = "No hay materiales. Presiona 'Sincronizar desde Nube' para descargarlos.",
                color = Color.Gray,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(listaMateriales) { material ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        ListItem(
                            headlineContent = { Text(material.nombre) },
                            supportingContent = {
                                Text("Stock: ${material.stockActual} | Costo: $${material.precioPorUnidad}")
                            },
                            trailingContent = { Text("ID: ${material.id}") }
                        )
                    }
                }
            }
        }
    }
}