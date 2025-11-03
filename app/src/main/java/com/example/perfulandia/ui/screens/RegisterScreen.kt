package com.example.perfulandia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.perfulandia.ui.navigation.Screen

@Composable
fun RegisterScreen(navController: NavController) {
    // Estados para cada campo del formulario
    var name by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var termsAccepted by rememberSaveable { mutableStateOf(false) }

    // Estados para controlar si un campo ha sido "tocado" para mostrar errores
    var nameTouched by rememberSaveable { mutableStateOf(false) }
    var addressTouched by rememberSaveable { mutableStateOf(false) }
    var emailTouched by rememberSaveable { mutableStateOf(false) }
    var passwordTouched by rememberSaveable { mutableStateOf(false) }
    var termsTouched by rememberSaveable { mutableStateOf(false) }

    // Estados de error para cada campo
    val nameError = nameTouched && name.isBlank()
    val addressError = addressTouched && address.isBlank()
    val emailError = emailTouched && email.isBlank()
    val passwordError = passwordTouched && password.isBlank()
    val termsError = termsTouched && !termsAccepted

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Crear Cuenta",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Campo de Nombre
        OutlinedTextField(
            value = name,
            onValueChange = { name = it; nameTouched = true },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = nameError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        if (nameError) {
            ErrorText("El nombre no puede estar vacío.")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Dirección
        OutlinedTextField(
            value = address,
            onValueChange = { address = it; addressTouched = true },
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = addressError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        if (addressError) {
            ErrorText("La dirección no puede estar vacía.")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Correo Electrónico
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; emailTouched = true },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = emailError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        if (emailError) {
            ErrorText("El correo no puede estar vacío.")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; passwordTouched = true },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = passwordError,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        if (passwordError) {
            ErrorText("La contraseña no puede estar vacía.")
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Checkbox de Términos y Condiciones
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = termsAccepted,
                onCheckedChange = { termsAccepted = it; termsTouched = true }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Acepto los Términos y Condiciones")
        }
        if (termsError) {
            ErrorText("Debes aceptar los términos y condiciones.")
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Botón de Registro
        Button(
            onClick = {
                // Marcar todos los campos como "tocados" para mostrar errores
                nameTouched = true
                addressTouched = true
                emailTouched = true
                passwordTouched = true
                termsTouched = true

                val formIsValid = name.isNotBlank() && address.isNotBlank() && email.isNotBlank() && password.isNotBlank() && termsAccepted

                if (formIsValid) {
                    // Aquí iría tu lógica de registro (ej. llamar a un ViewModel)
                    // viewModel.register(name, address, email, password)

                    // Entrar a HOME
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                // Navegar de regreso a Login
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
        ) {
            Text("¿Ya tienes una cuenta? Inicia sesión")
        }
    }
}

@Composable
private fun ErrorText(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.error,
        fontSize = 12.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 4.dp)
    )
}
