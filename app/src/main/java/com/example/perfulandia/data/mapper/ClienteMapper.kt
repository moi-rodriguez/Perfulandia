package com.example.perfulandia.data.mapper

import com.example.perfulandia.data.remote.dto.ClienteDto
import com.example.perfulandia.model.Cliente

/**
 * ClienteMapper: Convierte entre ClienteDto (capa de datos) y Cliente (modelo de dominio)
 *
 * Responsabilidad:
 * - Mapear ClienteDto → Cliente para usar en la UI
 * - Mapear Cliente → ClienteDto para enviar al backend (si es necesario)
 */
object ClienteMapper {

    /**
     * Convierte ClienteDto (del backend) a Cliente (modelo de dominio)
     *
     * @param dto ClienteDto desde la API
     * @return Cliente modelo de dominio
     */
    fun fromDto(dto: ClienteDto): Cliente {
        return Cliente(
            id = dto._id ?: "",
            nombre = dto.nombre,
            descripcion = dto.descripcion,
            imagen = dto.imagen,
            imagenThumbnail = dto.imagenThumbnail,
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt
        )
    }

    /**
     * Convierte Cliente (modelo de dominio) a ClienteDto (para el backend)
     * Útil para operaciones de actualización
     *
     * @param cliente Modelo de dominio
     * @return ClienteDto para enviar a la API
     */
    fun toDto(cliente: Cliente): ClienteDto {
        return ClienteDto(
            _id = cliente.id,
            nombre = cliente.nombre,
            descripcion = cliente.descripcion,
            imagen = cliente.imagen,
            imagenThumbnail = cliente.imagenThumbnail,
            createdAt = cliente.createdAt,
            updatedAt = cliente.updatedAt
        )
    }

    /**
     * Convierte una lista de ClienteDto a lista de Cliente
     *
     * @param dtoList Lista de DTOs desde la API
     * @return Lista de modelos de dominio
     */
    fun fromDtoList(dtoList: List<ClienteDto>): List<Cliente> {
        return dtoList.map { fromDto(it) }
    }

    /**
     * Convierte una lista de Cliente a lista de ClienteDto
     *
     * @param clienteList Lista de modelos de dominio
     * @return Lista de DTOs para la API
     */
    fun toDtoList(clienteList: List<Cliente>): List<ClienteDto> {
        return clienteList.map { toDto(it) }
    }
}