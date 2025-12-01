package com.example.perfulandia.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.perfulandia.model.Perfume
import com.example.perfulandia.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController
) {
    // Lista harcodeada de perfumes con género
    val hardcodedPerfumes = listOf(
        Perfume(id = "1", nombre = "Chanel N°5", genero = "Femenino", descripcion = "El perfume de la eternidad.", imagen = "https://media.falabella.com/falabellaCL/270202_1/w=1500,h=1500,fit=cover"),
        Perfume(id = "2", nombre = "Dior Sauvage", genero = "Masculino", descripcion = "Una frescura radical y noble.", imagen = "https://cdnx.jumpseller.com/sairam/image/5929373/thumb/1500/1500?1654111131"),
        Perfume(id = "3", nombre = "Creed Aventus", genero = "Masculino", descripcion = "Fuerza, poder y éxito.", imagen = "https://cl-cenco-pim-resizer.ecomm.cencosud.com/unsafe/adaptive-fit-in/3840x0/filters:quality(75)/prd-cl/product-medias/6c2b0238-d04c-47c4-afb4-fb02ecc0773d/MKNFQHO2Y6/MKNFQHO2Y6-1/1758642147254-MKNFQHO2Y6-1-0.jpg"),
        Perfume(id = "4", nombre = "Light Blue D&G", genero = "Femenino", descripcion = "La quintaesencia de la alegría de vivir.", imagen = "https://media.falabella.com/falabellaCL/4750703_1/w=1500,h=1500,fit=cover"),
        Perfume(id = "5", nombre = "Black Opium YSL", genero = "Femenino", descripcion = "Un chute de adrenalina.", imagen = "https://cdnx.jumpseller.com/sairam/image/43732462/thumb/1500/1500?1703002665"),
        Perfume(id = "6", nombre = "CK One", genero = "Unisex", descripcion = "Un perfume para todos.", imagen = "https://static.beautytocare.com/media/catalog/product/c/a/calvin-klein-ck-one-eau-de-toilette-200ml_1.jpg"),
        Perfume(id = "7", nombre = "Paco Rabanne 1 Million", genero = "Masculino", descripcion = "El aroma del éxito.", imagen = "https://cdnx.jumpseller.com/sairam/image/9047411/thumb/1500/1500?1634971972")
    )

    var selectedGenero by remember { mutableStateOf<String?>("Todos") }

    val filteredPerfumes = remember(selectedGenero, hardcodedPerfumes) {
        if (selectedGenero == "Todos") {
            hardcodedPerfumes
        } else {
            hardcodedPerfumes.filter { it.genero == selectedGenero }
        }
    }

    // Navegación Inferior
    val items = listOf(Screen.Home, Screen.Profile)
    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfulandia") },
                actions = {
                    IconButton(onClick = { /* TODO: Navegar al Carrito */ }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
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
                            if (screen != Screen.Home) {
                                navController.navigate(screen.route) {
                                    popUpTo(Screen.Home.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        label = { Text(if(screen == Screen.Home) "Inicio" else "Perfil") },
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            // --- FILTROS POR GÉNERO ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val generos = listOf("Todos", "Masculino", "Femenino", "Unisex")
                generos.forEach { genero ->
                    FilterChip(
                        selected = selectedGenero == genero,
                        onClick = { selectedGenero = genero },
                        label = { Text(genero) }
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredPerfumes) { perfume ->
                    PerfumeCard(perfume = perfume) {
                        // navController.navigate("detail/${perfume.id}")
                    }
                }
            }
        }
    }
}


/**
 * Componente individual para mostrar un perfume
 */
@Composable
fun PerfumeCard(
    perfume: Perfume,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(perfume.getImageUrl() ?: "")
                    .crossfade(true)
                    .build(),
                contentDescription = perfume.nombre,
                modifier = Modifier
                    .height(140.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = perfume.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = perfume.genero ?: "",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.secondary
            )

            Text(
                text = "$ 29.990", // Precio simulado
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { /* Acción Agregar */ },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Text("Ver", fontSize = 12.sp)
            }
        }
    }
}