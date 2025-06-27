package org.spdgrupo.elbuensaborapi.service.utils;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spdgrupo.elbuensaborapi.model.entity.Promocion;
import org.spdgrupo.elbuensaborapi.repository.PromocionRepository;
import org.spdgrupo.elbuensaborapi.service.InsumoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);
    private final PromocionRepository promocionRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void actualizarEstadoPromociones() {
        List<Promocion> promociones = promocionRepository.findAll();

        for (Promocion promocion : promociones) {
            LocalDate now = LocalDate.now();

            boolean estadoActivo = now.isAfter(promocion.getFechaDesde().minusDays(1))
                    && now.isBefore(promocion.getFechaHasta().plusDays(1));
            promocion.setActivo(estadoActivo);
            promocionRepository.save(promocion);
        }

        LOGGER.info("Se actualizaron {} promociones correctamente.", promociones.size());
    }


}
