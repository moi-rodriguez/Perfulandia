package com.example.perfulandia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.perfulandia.viewmodel.CreateReviewViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReviewScreen(
    navController: NavController,
    perfumeId: String,
    viewModel: CreateReviewViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    // Navegar hacia atrás si la reseña se creó con éxito
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Escribir Reseña") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Calificación", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            // Selector de estrellas
            Row(horizontalArrangement = Arrangement.Center) {
                (1..5).forEach { starIndex ->
                    IconButton(onClick = { viewModel.onRatingChange(starIndex) }) {
                        Icon(
                            imageVector = if (starIndex <= uiState.rating) Icons.Filled.Star else Icons.Outlined.StarOutline,
                            contentDescription = "Estrella $starIndex",
                            tint = if (starIndex <= uiState.rating) Color.Yellow else Color.Gray,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = uiState.comment,
                onValueChange = { viewModel.onCommentChange(it) },
                label = { Text("Comentario (opcional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        // TODO: El ID del cliente debe obtenerse de la sesión (SessionManager)
                        // Por ahora, se usa un valor hardcodeado como placeholder.
                        val placeholderClientId = "654f1b8a8b1ce5273f629f60"
                        viewModel.submitReview(perfumeId, placeholderClientId)
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Enviar Reseña")
                }
            }

            uiState.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}