// File: app/src/main/java/com/example/perfulandia/data/AvatarRepository.kt
package com.example.perfulandia.data

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para crear una instancia de DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "avatar_prefs")

class AvatarRepository(private val context: Context) {

    // Clave para almacenar la URI como un String
    private val avatarUriKey = stringPreferencesKey("avatar_uri")

    // Flujo para obtener la URI de la imagen. Se convierte de String a Uri.
    val avatarUri: Flow<Uri?> = context.dataStore.data
        .map { preferences ->
            preferences[avatarUriKey]?.let { Uri.parse(it) }
        }

    // Función para guardar la URI. Se convierte de Uri a String.
    suspend fun saveAvatarUri(uri: Uri?) {
        context.dataStore.edit { preferences ->
            if (uri != null) {
                preferences[avatarUriKey] = uri.toString()
            } else {
                // Si la URI es nula, eliminamos la clave para limpiar el avatar guardado
                preferences.remove(avatarUriKey)
            }
        }
    }
}
