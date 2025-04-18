package org.spdgrupo.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class RubroInsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String denominacion;
    private String unidadMedida;
    private boolean activo;

    @ManyToOne
    @JoinColumn(name = "rubro_padre_id")
    private RubroInsumo rubroPadre;
}
