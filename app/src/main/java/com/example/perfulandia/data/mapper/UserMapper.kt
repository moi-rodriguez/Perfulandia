package com.example.perfulandia.data.mapper

import com.example.perfulandia.data.remote.dto.UserDto
import com.example.perfulandia.model.User

/**
 * UserMapper: Convierte entre UserDto (capa de datos) y User (modelo de dominio)
 *
 * Responsabilidad:
 * - Mapear UserDto → User para usar en la UI
 * - Mapear User → UserDto para enviar al backend (si es necesario)
 */
object UserMapper {

    /**
     * Convierte UserDto (del backend) a User (modelo de dominio)
     *
     * @param dto UserDto desde la API
     * @return User modelo de dominio
     */
    fun fromDto(dto: UserDto): User {
        return User(
            id = dto._id,
            nombre = dto.nombre ?: "Usuario",
            email = dto.email,
            role = dto.role,
            telefono = dto.telefono,
            direccion = dto.direccion,
            preferencias = dto.preferencias ?: emptyList(),
            createdAt = dto.createdAt
        )
    }
}