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
        // Extraer el nombre de la categoría de forma segura, sea un String o un Objeto (Map)
        val categoriaNombre = when (dto.categoria) {
            is String -> dto.categoria
            is Map<*, *> -> dto.categoria["nombre"] as? String
            else -> null
        }

        return Perfume(
            id = dto._id ?: "", // MongoDB usa _id, lo convierte a id
            nombre = dto.nombre,
            marca = dto.marca,
            fragancia = dto.fragancia,
            tamano = dto.tamano,
            genero = dto.genero,
            precio = dto.precio ?: 0.0,
            stock = dto.stock ?: 0,
            categoriaId = categoriaNombre,
            descripcion = dto.descripcion,
            imagen = dto.imagen,
            imagenThumbnail = dto.imagenThumbnail,
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