package com.example.perfulandia.model

/**
 * Interfaz común para todas las entidades que tienen imagen y descripción.
 * Esto permite reutilizar componentes de UI (Cards) para Perfumes, Categorías, etc.
 */
interface BaseItem {
    val id: String
    val nombre: String
    val descripcion: String?
    val imagen: String?
    val imagenThumbnail: String?

    fun hasImage(): Boolean = !imagen.isNullOrEmpty()

    // Prioriza el thumbnail si existe, sino usa la imagen normal
    fun getImageUrl(): String? = if (!imagenThumbnail.isNullOrEmpty()) imagenThumbnail else imagen
}

/**
 * Modelo de dominio para Usuario
 */
data class User(
    val id: String,
    val nombre: String,
    val email: String,
    val role: String,
    val telefono: String? = null,
    val direccion: String? = null,
    val preferencias: List<String> = emptyList(),
    val createdAt: String? = null
) {
    fun isAdmin(): Boolean = role.equals("admin", ignoreCase = true)
    fun isUser(): Boolean = role.equals("user", ignoreCase = true) || role.equals("cliente", ignoreCase = true)
}

/**
 * Modelo de dominio para Perfume
 */
data class Perfume(
    override val id: String,
    override val nombre: String,
    val genero: String? = null,
    override val descripcion: String? = null,
    override val imagen: String? = null,
    override val imagenThumbnail: String? = null,
    val marca: String? = null,
    val precio: Double = 0.0,
    val stock: Int = 0,
    val createdAt: String? = null,
    val updatedAt: String? = null,
) : BaseItem

/**
 * Clase sellada para representar el estado de operaciones asíncronas en la UI
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}