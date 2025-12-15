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
        // Lógica para extraer ID y Nombre de la categoría
        val categoriaId: String?
        val categoriaNombre: String?

        when (dto.categoria) {
            is String -> {
                categoriaId = dto.categoria
                categoriaNombre = null // No tenemos el nombre si solo viene el ID
            }
            is Map<*, *> -> {
                categoriaId = dto.categoria["_id"] as? String
                categoriaNombre = dto.categoria["nombre"] as? String
            }
            else -> {
                categoriaId = null
                categoriaNombre = null
            }
        }

        return Perfume(
            id = dto._id ?: "",
            nombre = dto.nombre,
            marca = dto.marca,
            fragancia = dto.fragancia,
            tamano = dto.tamano,
            genero = dto.genero,
            precio = dto.precio ?: 0.0,
            stock = dto.stock ?: 0,
            categoriaId = categoriaId,
            categoriaNombre = categoriaNombre,
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

    /**
     * Convierte un objeto Perfume (modelo de dominio) a PerfumeDto (para enviar a la API)
     */
    fun toDto(perfume: Perfume): PerfumeDto {
        return PerfumeDto(
            _id = if (perfume.id.isNotEmpty()) perfume.id else null,
            nombre = perfume.nombre,
            marca = perfume.marca,
            fragancia = perfume.fragancia,
            tamano = perfume.tamano,
            genero = perfume.genero,
            precio = perfume.precio,
            stock = perfume.stock,
            // La API espera solo el ID de la categoría como String
            categoria = perfume.categoriaId,
            descripcion = perfume.descripcion,
            imagen = perfume.imagen,
            imagenThumbnail = perfume.imagenThumbnail
        )
    }
}