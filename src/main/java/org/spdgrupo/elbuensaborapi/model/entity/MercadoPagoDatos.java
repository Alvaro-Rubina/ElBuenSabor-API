package org.spdgrupo.elbuensaborapi.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class MercadoPagoDatos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


}
