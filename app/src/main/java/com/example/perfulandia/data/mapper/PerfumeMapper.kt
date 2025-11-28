package com.example.perfulandia.data.mapper

import com.example.perfulandia.data.remote.dto.PerfumeDto
import com.example.perfulandia.model.Perfume

/**
 * Convierte entre PerfumeDto (capa de datos) y Perfume (modelo de dominio)
 *
 * Responsabilidad:
 * - Mapear PerfumeDto -> Perfume para usar en la UI
 * - Mapear Perfume -> PerfumeDto para enviar al backend (si es necesario)
 *
 * Usa object (singleton) porque no necesita estado
 */
object PerfumeMapper {

    /**
     * Convierte PerfumeDto (del backend) a Perfume (modelo de dominio)
     *
     * @param dto PerfumeDto desde la API
     * @return Perfume modelo de dominio
     */
    fun fromDto(dto: PerfumeDto): Perfume {
        return Perfume(
            id = dto._id ?: "", // MongoDB usa _id, lo convierte a id
            nombre = dto.nombre,
            descripcion = dto.descripcion,
            imagen = dto.imagen,
            imagenThumbnail = dto.imagenThumbnail,
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt
        )
    }

    /**
     * Convierte Perfume (modelo de dominio) a PerfumeDto (para el backend)
     * Para operaciones de actualización
     *
     * @param perfume Modelo de dominio
     * @return PerfumeDto para enviar a la API
     */
    fun toDto(perfume: Perfume): PerfumeDto {
        return PerfumeDto(
            _id = perfume.id,
            nombre = perfume.nombre,
            descripcion = perfume.descripcion,
            imagen = perfume.imagen,
            imagenThumbnail = perfume.imagenThumbnail,
            createdAt = perfume.createdAt,
            updatedAt = perfume.updatedAt
        )
    }

    /**
     * Convierte una lista de PerfumeDto a lista de Perfume
     *
     * @param dtoList Lista de DTOs desde la API
     * @return Lista de modelos de dominio
     */
    fun fromDtoList(dtoList: List<PerfumeDto>): List<Perfume> {
        return dtoList.map { fromDto(it) }
    }

    /**
     * Convierte una lista de Perfume a lista de PerfumeDto
     *
     * @param perfumeList Lista de modelos de dominio
     * @return Lista de DTOs para la API
     */
    fun toDtoList(perfumeList: List<Perfume>): List<PerfumeDto> {
        return perfumeList.map { toDto(it) }
    }
}














