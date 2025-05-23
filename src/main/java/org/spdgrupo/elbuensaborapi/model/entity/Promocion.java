package org.spdgrupo.elbuensaborapi.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Promocion extends Base {

    private String denominacion;

    @Column(length = 500)
    private String urlImagen;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaDesde;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaHasta;

    private Double descuento;

    @OneToMany(mappedBy = "promocion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePromocion> detallePromociones;
}
