package com.example.perfulandia.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.perfulandia.AppDependencies
import com.example.perfulandia.model.Perfume
import com.example.perfulandia.ui.navigation.Screen
import com.example.perfulandia.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val dependencies = remember { AppDependencies.getInstance(context) }

    // Inyección del ViewModel
    val viewModel: HomeViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                HomeViewModel(
                    dependencies.perfumeRepository,
                    dependencies.categoriaRepository
                )
            }
        }
    )

    val uiState by viewModel.uiState.collectAsState()

    // Navegación Inferior
    val items = listOf(Screen.Home, Screen.Profile)
    var selectedItem by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfulandia") },
                actions = {
                    IconButton(onClick = { viewModel.loadData() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Recargar")
                    }
                    // Icono de carrito (visual por ahora)
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
                .fillMaxSize()
        ) {
            // --- 1. FILTROS (CHIPS) ---
            if (uiState.categories.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = uiState.selectedCategoryId == null,
                            onClick = { viewModel.selectCategory(null) },
                            label = { Text("Todos") }
                        )
                    }
                    items(uiState.categories) { categoria ->
                        FilterChip(
                            selected = uiState.selectedCategoryId == categoria.id,
                            onClick = { viewModel.selectCategory(categoria.id) },
                            label = { Text(categoria.nombre) }
                        )
                    }
                }
            }

            // --- 2. CONTENIDO (GRILLA O CARGA) ---
            Box(modifier = Modifier.fillMaxSize()) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (uiState.error != null) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Ups, algo falló", color = MaterialTheme.colorScheme.error)
                        Text(text = uiState.error ?: "", style = MaterialTheme.typography.bodySmall)
                        Button(onClick = { viewModel.loadData() }) { Text("Reintentar") }
                    }
                } else {
                    // GRILLA DE PRODUCTOS
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 160.dp),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(uiState.filteredPerfumes) { perfume ->
                            PerfumeCard(perfume = perfume) {
                                // Al hacer click en un producto (Futura implementación de Detalle)
                                // navController.navigate("detail/${perfume.id}")
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Componente individual para mostrar un perfume (Basado en tu diseño antiguo)
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
            // Imagen con Coil
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(perfume.getImageUrl() ?: "https://fimgs.net/mdimg/perfume/o.48100.jpg") // Imagen real o placeholder
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

            // Como tu modelo no tiene precio, mostramos la categoría o descripción corta
            // O puedes poner un precio fijo si es solo MVP visual
            Text(
                text = "$ 29.990", // Precio simulado o perfume.precio si existiera
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