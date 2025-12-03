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
            marca = dto.marca,
            genero = dto.genero,
            precio = dto.precio ?: 0.0,
            stock = dto.stock ?: 0,
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt
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
}