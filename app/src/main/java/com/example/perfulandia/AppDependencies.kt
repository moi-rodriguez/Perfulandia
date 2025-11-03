/*
    registrar AvatarRepository en Dependencies

    El proyecto usa un contenedor de dependencias centralizado.

    Explicación:
        Service Locator Pattern: El contenedor crea y gestiona una única instancia
        Singleton compartido: Todos los ViewModels usan la misma instancia
        Fácil de testear: Puedes reemplazar con mocks en tests

 */

package com.example.perfulandia

import android.app.Application
import com.example.perfulandia.data.AvatarRepository
/**
 * Clase contenedora para todas las dependencias de la aplicación.
 * A medida que la app crezca, puedes añadir más repositorios aquí.
 */
class AppDependencies(
    val avatarRepository: AvatarRepository
    /*
Ejemplo de cómo añadirías más dependencias en el futuro:
    val userRepository: UserRepository,
    val sessionManager: SessionManager,
    val database: AppDatabase,
    val apiService: AuthApiService,
    val userDao: UserDao,
     */
)

/**
 * Objeto singleton que gestiona la creación y el acceso a las dependencias.
 */
object Dependencies {

    private var _appDependencies: AppDependencies? = null

    /**
     * Propiedad pública para acceder al contenedor de dependencias.
     * Lanza una excepción si las dependencias no han sido inicializadas.
     */
    val appDependencies: AppDependencies
        get() = _appDependencies ?: throw IllegalStateException("Dependencies must be initialized")

    /**
     * Inicializa el contenedor de dependencias.
     * Debe ser llamado una sola vez desde la clase Application.
     * @param application El objeto Application de Android.
     */
    fun init(application: Application) {
        if (_appDependencies == null) {
            _appDependencies = buildDependencies(application)
        }
    }

    /**
     * Construye y devuelve el contenedor con todas las dependencias de la app.
     * @param application El objeto Application necesario para dependencias que requieren contexto.
     */
    private fun buildDependencies(application: Application): AppDependencies {
        // Crear AvatarRepository para persistencia del avatar
        val avatarRepository = AvatarRepository(application)

        // Cuando tengas más dependencias, las crearías aquí también.
        return AppDependencies(
            avatarRepository = avatarRepository
            /*
            val userRepository: UserRepository,
            val sessionManager: SessionManager,
            val database: AppDatabase,
            val apiService: AuthApiService,
            val userDao: UserDao,
             */
        )
    }
}
