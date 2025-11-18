package com.example.perfulandia.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * SessionManager: Guarda, recupera o elimina el token de autenticación de forma segura
 */
class SessionManager(private val context: Context) {

    companion object {
        // DataStore privado de la app
        private val Context.dataStore by preferencesDataStore(name = "session_prefs")

        // Claves para guardar los valores
        private val KEY_AUTH_TOKEN = stringPreferencesKey("auth_token")
        private val KEY_USER_ID = stringPreferencesKey("user_id")
    }

    /**
     * Guarda el token y el id del usuario
     */
    suspend fun saveSession(token: String, userId: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_AUTH_TOKEN] = token
            preferences[KEY_USER_ID] = userId
        }
    }

    /**
     * Recupera el token guardado (o null si no existe)
     */
    suspend fun getAuthToken(): String? {
        return context.dataStore.data
            .map { preferences -> preferences[KEY_AUTH_TOKEN] }
            .first()
    }

    /**
     * Elimina la sesión completa (cerrar sesión)
     */
    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_AUTH_TOKEN)
            preferences.remove(KEY_USER_ID)
        }
    }
}
