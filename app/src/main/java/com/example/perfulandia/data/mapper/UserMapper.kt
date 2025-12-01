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
            id = dto._id ?: "",
            nombre = dto.nombre ?: "Usuario",
            email = dto.email,
            role = dto.role,
            createdAt = dto.createdAt
        )
    }

    /**
     * Convierte User (modelo de dominio) a UserDto (para el backend)
     * Útil para operaciones de actualización
     *
     * @param user Modelo de dominio
     * @return UserDto para enviar a la API
     */
    fun toDto(user: User): UserDto {
        return UserDto(
            _id = user.id,
            nombre = user.nombre,
            email = user.email,
            role = user.role,
            createdAt = user.createdAt
        )
    }

    /**
     * Convierte una lista de UserDto a lista de User
     *
     * @param dtoList Lista de DTOs desde la API
     * @return Lista de modelos de dominio
     */
    fun fromDtoList(dtoList: List<UserDto>): List<User> {
        return dtoList.map { fromDto(it) }
    }

    /**
     * Convierte una lista de User a lista de UserDto
     *
     * @param userList Lista de modelos de dominio
     * @return Lista de DTOs para la API
     */
    fun toDtoList(userList: List<User>): List<UserDto> {
        return userList.map { toDto(it) }
    }
}