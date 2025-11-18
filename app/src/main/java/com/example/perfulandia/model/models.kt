package com.example.perfulandia.model

/**
Modelo de dominio para Usuario
Representa un usuario autenticado del sistema
*/
data class User(
    val id: String,
    val nombre: String,
    val email: String,
    val role: String,  // CLIENTE, PRODUCTOR, ADMIN
    val telefono: String? = null,
    val ubicacion: String? = null,
    val direccion: String? = null
) {
    // Verifica si el usuario es CLIENTE
    fun isCliente(): Boolean = role == "CLIENTE"

    // Verifica si el usuario es PRODUCTOR
    fun isProductor(): Boolean = role == "PRODUCTOR"

    // Verifica si el usuario es ADMIN
    fun isAdmin(): Boolean = role == "ADMIN"
}