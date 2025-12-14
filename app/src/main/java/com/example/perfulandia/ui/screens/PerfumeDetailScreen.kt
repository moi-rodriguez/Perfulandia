package com.example.perfulandia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.perfulandia.ui.navigation.Screen
import com.example.perfulandia.viewmodel.PerfumeDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfumeDetailScreen(
    navController: NavController,
    perfumeId: String,
    viewModel: PerfumeDetailViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    // Recargar datos cada vez que la pantalla se reanuda
    DisposableEffect(lifecycleOwner, perfumeId) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadPerfume(perfumeId)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Observar el evento de navegación al login
    LaunchedEffect(uiState.navigateToLogin) {
        if (uiState.navigateToLogin) {
            navController.navigate(Screen.Login.route)
            viewModel.onNavigationHandled() // Resetear el evento
        }
    }

    // Observar el evento para mostrar el Snackbar
    LaunchedEffect(uiState.showAddedToCartMessage) {
        if (uiState.showAddedToCartMessage) {
            snackbarHostState.showSnackbar("Añadido al carrito")
            viewModel.onMessageShown() // Resetear el evento
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(uiState.perfume?.nombre ?: "Detalle") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.error != null -> {
                    Text(
                        text = "Error: ${uiState.error}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.perfume != null -> {
                    val perfume = uiState.perfume!!

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(perfume.getImageUrl())
                                    .crossfade(true)
                                    .build(),
                                contentDescription = perfume.nombre,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = perfume.nombre,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "$ ${perfume.precio}",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Sección de Especificaciones
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            perfume.marca?.let {
                                Text("Marca: $it", style = MaterialTheme.typography.bodyLarge)
                            }
                            perfume.categoriaNombre?.let {
                                Text("Categoría: $it", style = MaterialTheme.typography.bodyLarge)
                            }
                            perfume.genero?.let {
                                Text("Género: $it", style = MaterialTheme.typography.bodyLarge)
                            }
                            perfume.tamano?.let {
                                Text("Tamaño: ${it}ml", style = MaterialTheme.typography.bodyLarge)
                            }
                            Text("Stock: ${perfume.stock}", style = MaterialTheme.typography.bodyLarge)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Descripción",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = perfume.descripcion ?: "No hay descripción disponible.",
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = 24.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { viewModel.onAddToCartClicked() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text("Agregar al Carrito", fontSize = 16.sp)
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // --- Sección de Reseñas ---
                        Text(
                            text = "Reseñas",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        if (uiState.reviews.isNotEmpty()) {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                uiState.reviews.forEach { review ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        elevation = CardDefaults.cardElevation(2.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            // Aquí podrías mostrar estrellas basadas en review.calificacion
                                            Text(
                                                text = "Calificación: ${review.calificacion} / 5",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = review.comentario,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            // Opcional: Nombre del autor
                                         Spacer(modifier = Modifier.height(8.dp))
                                         Text(
                                             text = "- ${review.autor}",
                                             style = MaterialTheme.typography.bodySmall,
                                             color = MaterialTheme.colorScheme.onSurfaceVariant
                                         )
                                        }
                                    }
                                }
                            }
                        } else {

                            Text(
                                text = "Todavía no hay reseñas para este perfume.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}