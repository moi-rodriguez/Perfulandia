package com.example.perfulandia.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.perfulandia.AppDependencies
import com.example.perfulandia.ui.navigation.Screen
import com.example.perfulandia.viewmodel.CartViewModel
import com.example.perfulandia.viewmodel.OrderPlacementState
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, viewModel: CartViewModel) {
    val context = LocalContext.current
    val sessionManager = AppDependencies.getInstance(context).sessionManager

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val orderState by viewModel.orderPlacementState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Guest User Check
    LaunchedEffect(Unit) {
        val userEmail = sessionManager.getUserEmail()
        if (userEmail == "invitado@sistema.com") {
            // Si el usuario es "Invitado", se le deniega el acceso, se cierra su sesión y se le redirige.
            Toast.makeText(context, "El usuario Invitado no puede acceder al carrito. Inicia sesión.", Toast.LENGTH_LONG).show()
            sessionManager.clearSession() // Se cierra la sesión del invitado.
            navController.navigate(Screen.Login.route) {
                // Limpia toodo el backstack para forzar un inicio de sesión limpio.
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    // Observe order placement state
    LaunchedEffect(orderState) {
        when (orderState) {
            is OrderPlacementState.Success -> {
                Toast.makeText(context, "¡Pedido realizado con éxito!", Toast.LENGTH_LONG).show()
                navController.navigate(Screen.OrderSuccess.route) {
                    popUpTo(Screen.Home.route) { inclusive = false } // Navigate back to home, keeping it on backstack
                }
                viewModel.resetOrderPlacementState()
            }
            is OrderPlacementState.Error -> {
                val errorMessage = (orderState as OrderPlacementState.Error).message
                snackbarHostState.showSnackbar(errorMessage)
                viewModel.resetOrderPlacementState()
            }
            else -> {} // Idle or Loading, do nothing specific here
        }
    }

    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-ES")) }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito") },
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
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.items.isEmpty()) {
                Text(
                    text = "Tu carrito está vacío.",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.items, key = { it.perfume.id }) { cartItem ->
                            CartItemCard(
                                cartItem = cartItem,
                                onQuantityIncrease = { viewModel.incrementQuantity(cartItem.perfume.id) },
                                onQuantityDecrease = { viewModel.decrementQuantity(cartItem.perfume.id) },
                                onRemoveItem = { viewModel.removeItem(cartItem.perfume.id) },
                                currencyFormat = currencyFormat
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Summary and Checkout Button
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "Total: ${currencyFormat.format(uiState.total)}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.placeOrder() },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = uiState.items.isNotEmpty() && orderState != OrderPlacementState.Loading,
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            if (orderState == OrderPlacementState.Loading) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                            } else {
                                Text("Confirmar Compra", style = MaterialTheme.typography.titleMedium)
                            }
                        }
                    }
                }
            }

            // Loading overlay for global loading, if needed (e.g. initial token check for guest)
            if (orderState == OrderPlacementState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun CartItemCard(
    cartItem: com.example.perfulandia.viewmodel.CartItem,
    onQuantityIncrease: () -> Unit,
    onQuantityDecrease: () -> Unit,
    onRemoveItem: () -> Unit,
    currencyFormat: NumberFormat
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(cartItem.perfume.getImageUrl())
                    .crossfade(true)
                    .build(),
                contentDescription = cartItem.perfume.nombre,
                modifier = Modifier
                    .size(80.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cartItem.perfume.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2
                )
                Text(
                    text = currencyFormat.format(cartItem.perfume.precio),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    OutlinedIconButton(
                        onClick = onQuantityDecrease,
                        modifier = Modifier.size(32.dp),
                        enabled = cartItem.quantity > 1
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Disminuir cantidad")
                    }
                    Text(
                        text = "${cartItem.quantity}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    OutlinedIconButton(
                        onClick = onQuantityIncrease,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Aumentar cantidad")
                    }
                }
            }

            IconButton(onClick = onRemoveItem) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar item",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}