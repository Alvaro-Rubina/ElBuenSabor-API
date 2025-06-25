package org.spdgrupo.elbuensaborapi.model.enums;

import java.util.EnumSet;
import java.util.Set;

public enum Estado {
    PENDIENTE_FACTURACION,
    SOLICITADO,
    EN_PREPARACION,
    TERMINADO,
    EN_CAMINO,
    ENTREGADO,
    CANCELADO;

    public Set<Estado> getEstadosPermitidos() {
        return switch (this) {
            case PENDIENTE_FACTURACION -> EnumSet.of(SOLICITADO, CANCELADO);
            case SOLICITADO -> EnumSet.of(EN_PREPARACION);
            case EN_PREPARACION -> EnumSet.of(TERMINADO);
            case TERMINADO -> EnumSet.of(EN_CAMINO, ENTREGADO);
            default -> EnumSet.noneOf(Estado.class); // No se puede avanzar desde otros
        };
    }

    public boolean puedeTransicionarA(Estado nuevoEstado) {
        return getEstadosPermitidos().contains(nuevoEstado);
    }
}