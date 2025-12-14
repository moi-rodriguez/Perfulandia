package com.example.perfulandia.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.perfulandia.ui.navigation.Screen

@Composable
fun AdminDashboardScreen(navController: NavController) {
    Scaffold(
        topBar = {
            // Puedes agregar un TopAppBar si lo deseas
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Panel de Administrador", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.navigate(Screen.CreatePerfume.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crear Perfume")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screen.ManageUsers.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gestionar Usuarios")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screen.ViewReports.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Reportes")
            }
        }
    }
}