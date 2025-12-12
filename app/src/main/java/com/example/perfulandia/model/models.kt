package com.example.perfulandia.model

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
)

/**
 * Modelo de dominio para Perfume
 */
data class Perfume(
    val id: String,
    val nombre: String,
    val marca: String? = null,
    val fragancia: String? = null,
    val tamano: Int? = null,
    val genero: String? = null,
    val precio: Double = 0.0,
    val stock: Int = 0,
    val categoriaId: String? = null,
    val descripcion: String? = null,
    val imagen: String? = null,
    val imagenThumbnail: String? = null
) {
    // Prioriza el thumbnail si existe, sino usa la imagen normal
    fun getImageUrl(): String? = if (!imagenThumbnail.isNullOrEmpty()) imagenThumbnail else imagen
}

/**
 * Clase sellada para representar el estado de operaciones asíncronas en la UI (Carga, Éxito, Error)
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}