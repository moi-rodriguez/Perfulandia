package com.example.perfulandia.data.mapper

import com.example.perfulandia.data.remote.dto.CategoriaDto
import com.example.perfulandia.model.Categoria

/**
 * CategoriaMapper: Convierte entre CategoriaDto (capa de datos) y Categoria (modelo de dominio)
 *
 * Responsabilidad:
 * - Mapear CategoriaDto → Categoria para usar en la UI
 * - Mapear Categoria → CategoriaDto para enviar al backend (si es necesario)
 */
object CategoriaMapper {

    /**
     * Convierte CategoriaDto (del backend) a Categoria (modelo de dominio)
     *
     * @param dto CategoriaDto desde la API
     * @return Categoria modelo de dominio
     */
    fun fromDto(dto: CategoriaDto): Categoria {
        return Categoria(
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
     * Convierte Categoria (modelo de dominio) a CategoriaDto (para el backend)
     * Útil para operaciones de actualización
     *
     * @param categoria Modelo de dominio
     * @return CategoriaDto para enviar a la API
     */
    fun toDto(categoria: Categoria): CategoriaDto {
        return CategoriaDto(
            _id = categoria.id,
            nombre = categoria.nombre,
            descripcion = categoria.descripcion,
            imagen = categoria.imagen,
            imagenThumbnail = categoria.imagenThumbnail,
            createdAt = categoria.createdAt,
            updatedAt = categoria.updatedAt
        )
    }

    /**
     * Convierte una lista de CategoriaDto a lista de Categoria
     *
     * @param dtoList Lista de DTOs desde la API
     * @return Lista de modelos de dominio
     */
    fun fromDtoList(dtoList: List<CategoriaDto>): List<Categoria> {
        return dtoList.map { fromDto(it) }
    }

    /**
     * Convierte una lista de Categoria a lista de CategoriaDto
     *
     * @param categoriaList Lista de modelos de dominio
     * @return Lista de DTOs para la API
     */
    fun toDtoList(categoriaList: List<Categoria>): List<CategoriaDto> {
        return categoriaList.map { toDto(it) }
    }
}