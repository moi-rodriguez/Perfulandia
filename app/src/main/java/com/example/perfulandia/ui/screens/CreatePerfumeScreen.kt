package com.example.perfulandia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.perfulandia.model.Category
import com.example.perfulandia.ui.navigation.Screen
import com.example.perfulandia.viewmodel.CreatePerfumeViewModel
import com.example.perfulandia.viewmodel.SubmissionStatus
import kotlinx.coroutines.delay
import androidx.compose.material3.MenuAnchorType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePerfumeScreen(
    navController: NavController,
    viewModel: CreatePerfumeViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val categories = uiState.categories

    var perfumeName by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var fragrance by remember { mutableStateOf("") }
    var size by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    
    var validationError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(categories) {
        if (categories.isNotEmpty() && selectedCategory == null) {
            selectedCategory = categories[0]
        }
    }

    LaunchedEffect(uiState.submissionStatus) {
        if (uiState.submissionStatus == SubmissionStatus.SUCCESS) {
            delay(2000) // Keep success message for 2 seconds
            // Clear form
            perfumeName = ""
            brand = ""
            fragrance = ""
            size = ""
            gender = ""
            price = ""
            stock = ""
            selectedCategory = categories.getOrNull(0)
            description = ""
            imageUrl = ""
            // Reset state in ViewModel
            viewModel.resetSubmissionStatus()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Nuevo Perfume") },
                actions = {
                    Button(onClick = {
                        viewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }) {
                        Text("Cerrar Sesión")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(value = perfumeName, onValueChange = { perfumeName = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = brand, onValueChange = { brand = it }, label = { Text("Marca") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = fragrance, onValueChange = { fragrance = it }, label = { Text("Fragancia") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = size, onValueChange = { size = it }, label = { Text("Tamaño (ml)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Género") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Precio") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Stock") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = imageUrl, onValueChange = { imageUrl = it }, label = { Text("URL de Imagen") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))

            // Category Dropdown
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedCategory?.nombre ?: "Cargando...",
                    onValueChange = { },
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.nombre) },
                            onClick = {
                                selectedCategory = category
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (validationError != null) {
                Text(validationError!!, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            if (uiState.submissionStatus == SubmissionStatus.SUCCESS) {
                Text("¡Perfume creado con éxito!", color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            if (uiState.submissionStatus == SubmissionStatus.ERROR && uiState.error != null) {
                Text("Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                onClick = {
                    validationError = null
                    val priceDouble = price.toDoubleOrNull()
                    val stockInt = stock.toIntOrNull()
                    val sizeInt = size.toIntOrNull()
                    val categoryId = selectedCategory?.id

                    if (perfumeName.isBlank() || brand.isBlank() || priceDouble == null || stockInt == null || categoryId == null) {
                        validationError = "Por favor, rellena todos los campos obligatorios."
                    } else {
                        viewModel.createPerfume(
                            nombre = perfumeName,
                            marca = brand,
                            fragancia = fragrance,
                            tamano = sizeInt,
                            genero = gender,
                            precio = priceDouble,
                            stock = stockInt,
                            categoriaId = categoryId,
                            descripcion = description,
                            imagen = imageUrl
                        )
                    }
                },
                enabled = uiState.submissionStatus != SubmissionStatus.SUBMITTING,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.submissionStatus == SubmissionStatus.SUBMITTING) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Enviar")
                }
            }
        }
    }
}
