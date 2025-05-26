package org.spdgrupo.elbuensaborapi.config.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Producto;

@Mapper(componentModel = "spring", uses = {RubroProductoMapper.class, DetalleProductoMapper.class})
public interface ProductoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "rubroId", target = "rubro.id")
    @Mapping(target = "precioCosto", ignore = true)
    @Mapping(target = "detalleProductos", ignore = true)
    Producto toEntity(ProductoDTO productoDTO);

    @Mapping(source = "rubro", target = "rubro")
    @Mapping(source = "detalleProductos", target = "detalleProductos")
    ProductoResponseDTO toResponseDTO(Producto producto);
}