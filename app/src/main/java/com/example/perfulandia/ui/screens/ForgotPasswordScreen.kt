package com.example.perfulandia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.perfulandia.ui.navigation.Screen

@Composable
fun ForgotPasswordScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var showSuccessMessage by remember { mutableStateOf(false) }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Recuperar Contraseña", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showSuccessMessage = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar")
            }

            if (showSuccessMessage) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Recibirás un enlace para recuperar tu contraseña.", color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.navigate(Screen.Login.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver al Login")
                }
            }
        }
    }
}