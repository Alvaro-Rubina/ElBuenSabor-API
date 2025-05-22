package org.spdgrupo.elbuensaborapi.model.dto.promocion;

import lombok.*;
import org.spdgrupo.elbuensaborapi.model.dto.detallepromocion.DetallePromocionDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PromocionPatchDTO {

    private String denominacion;
    private String urlImagen;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private Double descuento;
    private List<DetallePromocionDTO> detallePromociones = new ArrayList<>();
}
