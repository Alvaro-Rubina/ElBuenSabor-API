package org.spdgrupo.elbuensaborapi.model.dto.promocion;

import lombok.*;
import org.spdgrupo.elbuensaborapi.model.dto.detallepromocion.DetallePromocionResponseDTO;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PromocionResponseDTO {

    private Long id;
    private String denominacion;
    private String urlImagen;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private Double descuento;
    private List<DetallePromocionResponseDTO> detallePromociones;
}
