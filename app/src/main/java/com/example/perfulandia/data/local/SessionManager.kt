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
        userRole: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[KEY_AUTH_TOKEN] = token
            preferences[KEY_USER_ID] = userId
            preferences[KEY_USER_NAME] = userName
            preferences[KEY_USER_EMAIL] = userEmail
            preferences[KEY_USER_ROLE] = userRole
            preferences[KEY_IS_LOGGED_IN] = true
        }
    }

    /**
     * Actualiza solo el token (útil para refresh tokens)
     */
    suspend fun updateToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_AUTH_TOKEN] = token
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
     * Obtiene el ID del usuario
     */
    suspend fun getUserId(): String? {
        return context.dataStore.data
            .map { preferences -> preferences[KEY_USER_ID] }
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
     * Obtiene el rol del usuario
     */
    suspend fun getUserRole(): String? {
        return context.dataStore.data
            .map { preferences -> preferences[KEY_USER_ROLE] }
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

    /**
     * Obtiene toda la información de la sesión
     */
    suspend fun getSessionData(): SessionData? {
        return context.dataStore.data.map { preferences ->
            val token = preferences[KEY_AUTH_TOKEN]
            val userId = preferences[KEY_USER_ID]

            if (token != null && userId != null) {
                SessionData(
                    token = token,
                    userId = userId,
                    userName = preferences[KEY_USER_NAME] ?: "",
                    userEmail = preferences[KEY_USER_EMAIL] ?: "",
                    userRole = preferences[KEY_USER_ROLE] ?: "",
                    isLoggedIn = preferences[KEY_IS_LOGGED_IN] ?: false
                )
            } else {
                null
            }
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

    /**
     * Flow que emite el estado de autenticación
     */
    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[KEY_IS_LOGGED_IN] ?: false }

    /**
     * Flow que emite los datos completos de la sesión
     */
    val sessionDataFlow: Flow<SessionData?> = context.dataStore.data
        .map { preferences ->
            val token = preferences[KEY_AUTH_TOKEN]
            val userId = preferences[KEY_USER_ID]

            if (token != null && userId != null) {
                SessionData(
                    token = token,
                    userId = userId,
                    userName = preferences[KEY_USER_NAME] ?: "",
                    userEmail = preferences[KEY_USER_EMAIL] ?: "",
                    userRole = preferences[KEY_USER_ROLE] ?: "",
                    isLoggedIn = preferences[KEY_IS_LOGGED_IN] ?: false
                )
            } else {
                null
            }
        }

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

    /**
     * Elimina solo el token (mantiene info del usuario)
     * Útil para casos de seguridad
     */
    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_AUTH_TOKEN)
            preferences[KEY_IS_LOGGED_IN] = false
        }
    }

    // ============================================
    // UTILIDADES
    // ============================================

    /**
     * Verifica si el usuario es administrador
     */
    suspend fun isAdmin(): Boolean {
        val role = getUserRole()
        return role?.equals("admin", ignoreCase = true) ?: false
    }

    /**
     * Verifica si el token existe y no está vacío
     */
    suspend fun hasValidToken(): Boolean {
        val token = getAuthToken()
        return !token.isNullOrEmpty()
    }
}

/**
 * Data class que representa los datos de la sesión
 */
data class SessionData(
    val token: String,
    val userId: String,
    val userName: String,
    val userEmail: String,
    val userRole: String,
    val isLoggedIn: Boolean
)