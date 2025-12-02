package com.example.perfulandia.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * SessionManager: Maneja la sesión del usuario de forma segura usando DataStore
 *
 * Funcionalidades:
 * - Guarda y recupera el token JWT
 * - Guarda información del usuario (id, nombre, email, rol)
 * - Verifica si el usuario está autenticado
 * - Proporciona Flows reactivos para observar cambios
 */
class SessionManager(private val context: Context) {

    companion object {
        // DataStore privado de la app
        private val Context.dataStore by preferencesDataStore(name = "perfulandia_session")

        // Claves para guardar los valores
        private val KEY_AUTH_TOKEN = stringPreferencesKey("auth_token")
        private val KEY_USER_ID = stringPreferencesKey("user_id")
        private val KEY_USER_NAME = stringPreferencesKey("user_name")
        private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
        private val KEY_USER_ROLE = stringPreferencesKey("user_role")
        private val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val KEY_USER_PHONE = stringPreferencesKey("user_phone")
        private val KEY_USER_ADDRESS = stringPreferencesKey("user_address")
    }

    // ============================================
    // GUARDAR SESIÓN
    // ============================================

    /**
     * Guarda la sesión completa del usuario
     * @param token Token JWT de autenticación
     * @param userId ID del usuario
     * @param userName Nombre del usuario
     * @param userEmail Email del usuario
     * @param userRole Rol del usuario (admin, user, etc.)
     */
    suspend fun saveSession(
        token: String,
        userId: String,
        userName: String,
        userEmail: String,
        userRole: String,
        userPhone: String? = null,
        userAddress: String? = null
    ) {
        context.dataStore.edit { preferences ->
            preferences[KEY_AUTH_TOKEN] = token
            preferences[KEY_USER_ID] = userId
            preferences[KEY_USER_NAME] = userName
            preferences[KEY_USER_EMAIL] = userEmail
            preferences[KEY_USER_ROLE] = userRole
            preferences[KEY_IS_LOGGED_IN] = true
            preferences[KEY_USER_PHONE] = userPhone ?: ""
            preferences[KEY_USER_ADDRESS] = userAddress ?: ""
        }
    }

    // ============================================
    // OBTENER DATOS (Suspend - Una sola vez)
    // ============================================

    /**
     * Obtiene el token de autenticación
     * @return Token JWT o null si no existe
     */
    suspend fun getAuthToken(): String? {
        return context.dataStore.data
            .map { preferences -> preferences[KEY_AUTH_TOKEN] }
            .first()
    }

    /**
     * Obtiene el nombre del usuario
     */
    suspend fun getUserName(): String? {
        return context.dataStore.data
            .map { preferences -> preferences[KEY_USER_NAME] }
            .first()
    }

    /**
     * Obtiene el email del usuario
     */
    suspend fun getUserEmail(): String? {
        return context.dataStore.data
            .map { preferences -> preferences[KEY_USER_EMAIL] }
            .first()
    }

    /**
     * Verifica si el usuario está autenticado
     * @return true si hay una sesión activa
     */
    suspend fun isLoggedIn(): Boolean {
        return context.dataStore.data
            .map { preferences -> preferences[KEY_IS_LOGGED_IN] ?: false }
            .first()
    }

    // Función útil para recuperar toodo el perfil localmente (ej: modo offline)
    suspend fun getUserProfileData(): UserProfileData {
        return context.dataStore.data.map { prefs ->
            UserProfileData(
                name = prefs[KEY_USER_NAME] ?: "",
                email = prefs[KEY_USER_EMAIL] ?: "",
                role = prefs[KEY_USER_ROLE] ?: "",
                phone = prefs[KEY_USER_PHONE],
                address = prefs[KEY_USER_ADDRESS]
            )
        }.first()
    }

    // ============================================
    // FLOWS REACTIVOS (Observables)
    // ============================================

    /**
     * Flow que emite el token cada vez que cambia
     * Útil para observar cambios en tiempo real
     */
    val authTokenFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[KEY_AUTH_TOKEN] }


    // ============================================
    // LIMPIAR SESIÓN
    // ============================================

    /**
     * Elimina toda la sesión (cerrar sesión)
     */
    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_AUTH_TOKEN)
            preferences.remove(KEY_USER_ID)
            preferences.remove(KEY_USER_NAME)
            preferences.remove(KEY_USER_EMAIL)
            preferences.remove(KEY_USER_ROLE)
            preferences.remove(KEY_IS_LOGGED_IN)
        }
    }
}

/**
 * Data class simple para devolver datos agrupados
 */
data class UserProfileData(
    val name: String,
    val email: String,
    val role: String,
    val phone: String?,
    val address: String?
)