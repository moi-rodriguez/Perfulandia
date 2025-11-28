package com.example.perfulandia.data.mapper

import com.example.perfulandia.data.remote.dto.PedidoDto
import com.example.perfulandia.model.Pedido

/**
 * PedidoMapper: Convierte entre PedidoDto (capa de datos) y Pedido (modelo de dominio)
 *
 * Responsabilidad:
 * - Mapear PedidoDto → Pedido para usar en la UI
 * - Mapear Pedido → PedidoDto para enviar al backend (si es necesario)
 */
object PedidoMapper {

    /**
     * Convierte PedidoDto (del backend) a Pedido (modelo de dominio)
     *
     * @param dto PedidoDto desde la API
     * @return Pedido modelo de dominio
     */
    fun fromDto(dto: PedidoDto): Pedido {
        return Pedido(
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
     * Convierte Pedido (modelo de dominio) a PedidoDto (para el backend)
     * Útil para operaciones de actualización
     *
     * @param pedido Modelo de dominio
     * @return PedidoDto para enviar a la API
     */
    fun toDto(pedido: Pedido): PedidoDto {
        return PedidoDto(
            _id = pedido.id,
            nombre = pedido.nombre,
            descripcion = pedido.descripcion,
            imagen = pedido.imagen,
            imagenThumbnail = pedido.imagenThumbnail,
            createdAt = pedido.createdAt,
            updatedAt = pedido.updatedAt
        )
    }

    /**
     * Convierte una lista de PedidoDto a lista de Pedido
     *
     * @param dtoList Lista de DTOs desde la API
     * @return Lista de modelos de dominio
     */
    fun fromDtoList(dtoList: List<PedidoDto>): List<Pedido> {
        return dtoList.map { fromDto(it) }
    }

    /**
     * Convierte una lista de Pedido a lista de PedidoDto
     *
     * @param pedidoList Lista de modelos de dominio
     * @return Lista de DTOs para la API
     */
    fun toDtoList(pedidoList: List<Pedido>): List<PedidoDto> {
        return pedidoList.map { toDto(it) }
    }
}