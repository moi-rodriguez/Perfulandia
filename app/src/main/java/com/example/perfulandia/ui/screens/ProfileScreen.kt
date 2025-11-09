package com.example.perfulandia.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.perfulandia.ui.components.ImagePickerDialog
import com.example.perfulandia.ui.navigation.Screen
import com.example.perfulandia.viewmodel.ProfileViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.listOf

// --- Composable Principal ---
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    var showImagePicker by remember { mutableStateOf(false) } // showdialog
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) } // tempuri
    val items = listOf(Screen.Home, Screen.Profile)
    var selectedItem by remember { mutableIntStateOf(1) }

    // --- Permisos ---
    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_MEDIA_IMAGES
        )
    } else {
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    val permissionsState = rememberMultiplePermissionsState(permissions)

    // --- Lanzadores de Actividades ---

    // launcher para capturar foto con cámara
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            viewModel.updateAvatar(tempCameraUri)
        }
    }

    // launcher para seleccionar imagen de galería
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.updateAvatar(it)
        }
    }

    // cuadro de diálogo
    if (showImagePicker) {
        // se creó en ui.components
        ImagePickerDialog(
            onDismiss = { showImagePicker = false },
            onCameraClick = {
                showImagePicker = false
                // verficar si se ha concedio permiso
                if (permissionsState.permissions.any {
                        it.permission == Manifest.permission.CAMERA && it.status == PermissionStatus.Granted
                    }) {
                    // si se ha concedido, crea el archivo y abre la cámara
                    tempCameraUri = createImageUri(context)
                    tempCameraUri?.let { takePictureLauncher.launch(it) }
                } else {
                    // sino, pide los permisos denuevo
                    permissionsState.launchMultiplePermissionRequest()
                }
            },
            onGalleryClick = {
                showImagePicker = false
                val imagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.READ_MEDIA_IMAGES
                } else {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }

                if (permissionsState.permissions.any {
                        it.permission == imagePermission && it.status == PermissionStatus.Granted
                    }) {
                    // lanzar selector de galería
                    pickImageLauncher.launch("image/*")
                } else {
                    // solicitar permiso
                    permissionsState.launchMultiplePermissionRequest()
                }
            }
        )
    }

    // UI

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mi Perfil") })
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            if (navController.currentDestination?.route != screen.route) {
                                navController.navigate(screen.route)
                            }
                        },
                        label = { Text(screen.route) },
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
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // --- Avatar ---
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        // Solicitar permisos y luego mostrar el diálogo
                        val allPermissionsGranted = permissionsState.permissions.all {
                            it.status == PermissionStatus.Granted
                        }
                        if (allPermissionsGranted) {
                            showImagePicker = true
                        } else {
                            permissionsState.launchMultiplePermissionRequest()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (uiState.avatarUri != null) {
                    AsyncImage(
                        model = uiState.avatarUri,
                        contentDescription = "Avatar del usuario",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Seleccionar avatar",
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Loading simple
            if (uiState.isLoading) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Error
            if (uiState.error != null) {
                Text(
                    text = uiState.error ?: "",
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Nombre y correo del usuario (desde /auth/me)
            uiState.user?.let { user ->
                Text(text = user.name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = user.email, style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Botón Cerrar sesión
            Button(
                onClick = {
                    viewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}

/**
 * Crea un URI temporal para guardar la foto capturada por la cámara
 */
fun createImageUri(context: Context): Uri? {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "profile_avatar_$timeStamp.jpg"
    val storageDir = context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)

    return try {
        val imageFile = File(storageDir, imageFileName)
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            imageFile
        )
    } catch (e: Exception) {
        null
    }
}
