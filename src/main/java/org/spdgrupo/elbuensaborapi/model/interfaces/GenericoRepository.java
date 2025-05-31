package org.spdgrupo.elbuensaborapi.model.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface GenericoRepository<E , ID extends Serializable>extends JpaRepository<E,ID> {
}
