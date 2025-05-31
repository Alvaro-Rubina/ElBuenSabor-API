package org.spdgrupo.elbuensaborapi.repository;

import org.spdgrupo.elbuensaborapi.model.entity.RubroInsumo;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RubroInsumoRepository extends GenericoRepository<RubroInsumo, Long> {
}
