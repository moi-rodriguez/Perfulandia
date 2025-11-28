package com.example.perfulandia.model

/*
    Modelos
    - User:
        id, nombre, email, role createdAt
        métodos: isAdmin(), isUser()

    - Entidades (Perfume, Categoria, Cliente, Pedido, Resena):
        Siguen el EntityBase del backend:
        id, nombre, descripcion, imagen, imagenThumbnail, createdAt, updatedAt
        métodos: hasImage(), getImageUrl()

    - resource:
        Clase sellada para manejar estados en la UI: Success, Error, Loading
 */

/**
 * Modelo de dominio para Usuario
 * Representa un usuario autenticado del sistema
*/
data class User(
    val id: String,
    val nombre: String,
    val email: String,
    val role: String,  // "admin" o "user"
    val createdAt: String? = null
) {
    /**
     * Verifica si el usuario es admin
     */
    fun isAdmin(): Boolean = role.equals("admin", ignoreCase = true)

    /**
     * Verifica si el usuario es user
     */
    fun isUser(): Boolean = role.equals("user", ignoreCase = true)
}

/**
 * Modelo de dominio para Perfume
 */
data class Perfume(
    val id: String,
    val nombre: String,
    val descripcion: String? = null,
    val imagen: String? = null,
    val imagenThumbnail: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
) {
    /**
     * Verifica si tiene imagen
     */
    fun hasImage(): Boolean = !imagen.isNullOrEmpty()

    /**
     * Obtiene la URL de imagen o thumbnail, priorizando el thumbnail
     */
    fun getImageUrl(): String? = imagenThumbnail ?: imagen
}

/**
 * Modelo de dominio para Categoria
 */
data class Categoria(
    val id: String,
    val nombre: String,
    val descripcion: String? = null,
    val imagen: String? = null,
    val imagenThumbnail: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
) {
    fun hasImage(): Boolean = !imagen.isNullOrEmpty()
    fun getImageUrl(): String? = imagenThumbnail ?: imagen
}

/**
 * Modelo de dominio para Cliente
 */
data class Cliente(
    val id: String,
    val nombre: String,
    val descripcion: String? = null,
    val imagen: String? = null,
    val imagenThumbnail: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
) {
    fun hasImage(): Boolean = !imagen.isNullOrEmpty()
    fun getImageUrl(): String? = imagenThumbnail ?: imagen
}

/**
 * Modelo de dominio para Pedido
 */
data class Pedido(
    val id: String,
    val nombre: String,
    val descripcion: String? = null,
    val imagen: String? = null,
    val imagenThumbnail: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
) {
    fun hasImage(): Boolean = !imagen.isNullOrEmpty()
    fun getImageUrl(): String? = imagenThumbnail ?: imagen
}

/**
 * Modelo de dominio para Reseña
 */
data class Resena(
    val id: String,
    val nombre: String,
    val descripcion: String? = null,
    val imagen: String? = null,
    val imagenThumbnail: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
) {
    fun hasImage(): Boolean = !imagen.isNullOrEmpty()
    fun getImageUrl(): String? = imagenThumbnail ?: imagen
}

/**
 * Clase sellada para representar el estado de operaciones asíncronas
 * Útil para ViewModels y UI
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}