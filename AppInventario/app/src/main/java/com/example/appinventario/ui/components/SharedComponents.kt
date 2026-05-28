package com.example.appinventario.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.appinventario.R
import com.example.appinventario.data.local.entities.LlaveroEntity
import com.example.appinventario.ui.theme.AppColors

// TOP BAR
@Composable
fun InventarioTopBar(
    titulo: String,
    imagenRes: Int,
    drawerState: DrawerState? = null,
    onToggleDrawer: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .statusBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imagenRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Text(
            text = titulo,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.TextOnDark
        )

        if (onToggleDrawer != null && drawerState != null) {
            IconButton(
                onClick = onToggleDrawer,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = if (drawerState.isOpen) Icons.Default.Close else Icons.Default.Menu,
                    contentDescription = null,
                    tint = AppColors.White
                )
            }
        }
    }
}

// BOTON AÑADIR
@Composable
fun BotonAnadir(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(48.dp),
        shape = CircleShape,
        containerColor = AppColors.RedDark,
        contentColor = AppColors.TextOnDark
    ) {
        Icon(Icons.Default.Add, contentDescription = "Agregar producto")
    }
}

// MENU LATERAL
data class OpcionMenu(
    val etiqueta: String,
    val onClick: () -> Unit,
    val esDestructiva: Boolean = false
)

@Composable
fun MenuLateral(
    drawerState: DrawerState,
    opciones: List<OpcionMenu>,
    onCerrar: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier
            .width(220.dp)
            .padding(top = 82.dp),
        drawerContainerColor = AppColors.RedDark,
        drawerShape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = 16.dp,
            bottomEnd = 16.dp
        ),
        drawerTonalElevation = 8.dp
    ) {

        opciones.forEach { opcion ->
            TextButton(
                onClick = opcion.onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (opcion.esDestructiva) AppColors.BrownMid else AppColors.Cream
                )
            ) {
                Text(
                    text = opcion.etiqueta,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = AppColors.Cream.copy(alpha = 0.2f)
            )
        }
    }
}

// BARRA DE BÚSQUEDA
@Composable
fun BarraBusqueda(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        placeholder = { Text("Buscar...", color = Color.Gray) },
        shape = RoundedCornerShape(15.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = AppColors.White,
            unfocusedContainerColor = AppColors.White,
            focusedBorderColor = AppColors.BrownLight,
            unfocusedBorderColor = AppColors.BrownLight.copy(alpha = 0.5f),
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        singleLine = true
    )
}

// PRODUCTO CARD

@Composable
fun ProductoCard(
    llavero: LlaveroEntity,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.clickable { onClick() },
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
            Text(
                text = llavero.nombre,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextOnDark,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = if (llavero.descripcion.length > 60) llavero.descripcion.take(60) + "..." else llavero.descripcion,
                fontSize = 14.sp,
                color = AppColors.TextOnCard,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "$${String.format("%.2f", llavero.precioVenta)}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextOnDark
            )
        }
    }
}

// STOCK BADGE

@Composable
fun StockBadge(stockActual: Double, stockMinimo: Double) {
    val enEscasez = stockActual <= stockMinimo
    val color = if (enEscasez) AppColors.StockLow else AppColors.StockOk
    val texto = if (enEscasez) "Stock bajo" else "En stock"

    Box(
        modifier = Modifier
            .background(color = color, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(texto, fontSize = 11.sp, color = AppColors.White, fontWeight = FontWeight.Medium)
    }
}

//DIÁLOGO DE DETALLES DEL PRODUCTO

@Composable
fun ProductoDetallesDialog(
    llavero: LlaveroEntity,
    onCerrar: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onCerrar() }
    ) {
        Dialog(
            onDismissRequest = onCerrar,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .height(600.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = AppColors.Cream),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(250.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(AppColors.BrownLight.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("IMG", fontSize = 40.sp, color = AppColors.BrownText.copy(alpha = 0.5f))
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = llavero.nombre,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.BrownText,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "$${String.format("%.2f", llavero.precioVenta)}",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.BrownLight
                    )

                    Spacer(Modifier.height(12.dp))

                    HorizontalDivider(color = AppColors.BrownLight.copy(alpha = 0.3f), thickness = 1.dp)

                    Spacer(Modifier.height(12.dp))

                    Text("Descripción:", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AppColors.BrownText)

                    Spacer(Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 80.dp, max = 150.dp)
                    ) {
                        Text(
                            text = llavero.descripcion,
                            fontSize = 14.sp,
                            color = AppColors.BrownSub,
                            lineHeight = 20.sp
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = onCerrar,
                        modifier = Modifier.width(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.BrownText)
                    ) {
                        Text("Cerrar", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}