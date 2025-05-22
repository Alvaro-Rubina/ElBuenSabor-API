package org.spdgrupo.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class RubroInsumo extends Base {

    private String denominacion;

    private Boolean activo;

    @ManyToOne
    @JoinColumn(name = "rubro_padre_id")
    private RubroInsumo rubroPadre;

    @OneToMany(mappedBy = "rubroPadre")
    private List<RubroInsumo> subRubros = new ArrayList<>();
}
