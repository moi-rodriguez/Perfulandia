package com.example.perfulandia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.perfulandia.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val items = listOf(Screen.Home, Screen.Profile)
    var selectedItem by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            // barra superior para el título
            TopAppBar(
                title = { Text("Perfulandia") },
                actions = {
                    // Botón con el ícono del carrito
                    IconButton(onClick = { /* Navegar a la pantalla del carrito */ }) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Carrito de compras"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            if (navController.currentDestination?.route != screen.route) {
                                navController.navigate(screen.route)
                            }
                        },
                        label = { Text(screen.route) },
                        icon = {
                            Icon(
                                imageVector = if (screen == Screen.Home) Icons.Default.Home else Icons.Default.Person,
                                contentDescription = screen.route
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // Contenedor principal que centra la tarjeta
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp), // Un poco de espacio alrededor
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // --- INICIO DE LA TARJETA DE PRODUCTO ---
            Card(
                modifier = Modifier
                    .width(200.dp), // Ancho fijo para la tarjeta
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Imagen del producto (usando Coil)
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            // URL de ejemplo para la imagen
                            .data("https://fimgs.net/mdimg/perfume/o.48100.jpg")
                            .crossfade(true)
                            .build(),
                        contentDescription = "Imagen de producto",
                        modifier = Modifier
                            .height(140.dp)
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Nombre del producto
                    Text(
                        text = "Dior Sauvage",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Precio del producto
                    Text(
                        text = "$25.99",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Botón de agregar
                    Button(
                        onClick = { /* Lógica para agregar al carrito va aquí */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Agregar")
                    }
                }
            }
            // --- FIN DE LA TARJETA DE PRODUCTO ---
        }
    }
}
