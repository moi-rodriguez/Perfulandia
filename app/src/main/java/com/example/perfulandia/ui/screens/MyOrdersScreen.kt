package com.example.perfulandia.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.perfulandia.model.Order
import com.example.perfulandia.model.Perfume
import com.example.perfulandia.ui.navigation.Screen
import com.example.perfulandia.viewmodel.OrderViewModel

@Composable
fun MyOrdersScreen(
    navController: NavController,
    viewModel: OrderViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.loadMyOrders()
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else if (uiState.error != null) {
                Text(text = "Error: ${uiState.error}")
            } else if (uiState.orders.isEmpty()) {
                Text(text = "Aún no tienes pedidos.")
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.orders) { order ->
                        OrderCard(order = order, navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: Order, navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Pedido #${order.id.take(6)}", fontWeight = FontWeight.Bold)
            Text("Fecha: ${order.fecha}")
            Text("Total: $${order.total}", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(16.dp))

            Text("Artículos:", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Column {
                order.perfumes.forEach { perfume ->
                    PerfumeItemRow(perfume = perfume, navController = navController)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun PerfumeItemRow(perfume: Perfume, navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(perfume.nombre) // La cantidad no está disponible en el modelo `Perfume` actual
        Button(onClick = { navController.navigate(Screen.CreateReview.createRoute(perfume.id)) }) {
            Text("Opinar")
        }
    }
}