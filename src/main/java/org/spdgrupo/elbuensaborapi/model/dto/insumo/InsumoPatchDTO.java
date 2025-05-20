package org.spdgrupo.elbuensaborapi.model.dto.insumo;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.enums.UnidadMedida;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InsumoPatchDTO {

    private String denominacion;
    private String urlImagen;
    private Double precioCosto;
    private Double precioVenta;
    private Double stockActual;
    private Double stockMinimo;
    private Boolean esParaElaborar;
    private Boolean activo;
    private UnidadMedida unidadMedida;
    private Long rubroId;
}
