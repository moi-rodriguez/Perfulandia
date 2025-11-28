package com.example.perfulandia.data.mapper

import com.example.perfulandia.data.remote.dto.ResenaDto
import com.example.perfulandia.model.Resena

/**
 * ResenaMapper: Convierte entre ResenaDto (capa de datos) y Resena (modelo de dominio)
 *
 * Responsabilidad:
 * - Mapear ResenaDto → Resena para usar en la UI
 * - Mapear Resena → ResenaDto para enviar al backend (si es necesario)
 */
object ResenaMapper {

    /**
     * Convierte ResenaDto (del backend) a Resena (modelo de dominio)
     *
     * @param dto ResenaDto desde la API
     * @return Resena modelo de dominio
     */
    fun fromDto(dto: ResenaDto): Resena {
        return Resena(
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
     * Convierte Resena (modelo de dominio) a ResenaDto (para el backend)
     * Útil para operaciones de actualización
     *
     * @param resena Modelo de dominio
     * @return ResenaDto para enviar a la API
     */
    fun toDto(resena: Resena): ResenaDto {
        return ResenaDto(
            _id = resena.id,
            nombre = resena.nombre,
            descripcion = resena.descripcion,
            imagen = resena.imagen,
            imagenThumbnail = resena.imagenThumbnail,
            createdAt = resena.createdAt,
            updatedAt = resena.updatedAt
        )
    }

    /**
     * Convierte una lista de ResenaDto a lista de Resena
     *
     * @param dtoList Lista de DTOs desde la API
     * @return Lista de modelos de dominio
     */
    fun fromDtoList(dtoList: List<ResenaDto>): List<Resena> {
        return dtoList.map { fromDto(it) }
    }

    /**
     * Convierte una lista de Resena a lista de ResenaDto
     *
     * @param resenaList Lista de modelos de dominio
     * @return Lista de DTOs para la API
     */
    fun toDtoList(resenaList: List<Resena>): List<ResenaDto> {
        return resenaList.map { toDto(it) }
    }
}