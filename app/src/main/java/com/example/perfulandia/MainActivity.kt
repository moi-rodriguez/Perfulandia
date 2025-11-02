package com.example.perfulandia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.perfulandia.ui.navigation.AppNavigation
import com.example.perfulandia.ui.theme.PerfulandiaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PerfulandiaTheme {
                // Scaffold útil para aplicar padding y un layout base.
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Llama al composable de navegación
                    AppNavigation(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}