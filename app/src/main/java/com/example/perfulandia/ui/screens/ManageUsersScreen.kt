package com.example.perfulandia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.example.perfulandia.AppDependencies
import com.example.perfulandia.model.User
import com.example.perfulandia.viewmodel.ManageUsersViewModel

@OptIn(ExperimentalMaterial3Api::class) // Annotation added to fix the experimental API error
@Composable
fun ManageUsersScreen(navController: NavController) { 
    val context = LocalContext.current
    val dependencies = AppDependencies.getInstance(context)

    val viewModel: ManageUsersViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                ManageUsersViewModel(dependencies.userRepository)
            }
        }
    )

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestionar Usuarios") },
                navigationIcon = { // Added back button for better UX
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.error != null) {
                Text(
                    text = "Error: ${uiState.error}",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(uiState.users) { user ->
                        UserListItem(user = user)
                        HorizontalDivider() // Replaced deprecated Divider
                    }
                }
            }
        }
    }
}

@Composable
fun UserListItem(user: User) {
    ListItem(
        headlineContent = { Text(user.nombre) },
        supportingContent = { Text(user.email) },
        trailingContent = {
            Text(user.role, style = MaterialTheme.typography.bodySmall)
        }
    )
}