package org.spdgrupo.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
public class RubroInsumo extends Base {

    private String denominacion;

    @ManyToOne
    @JoinColumn(name = "rubro_padre_id")
    private RubroInsumo rubroPadre;

    @OneToMany(mappedBy = "rubroPadre")
    private List<RubroInsumo> subRubros = new ArrayList<>();
}
