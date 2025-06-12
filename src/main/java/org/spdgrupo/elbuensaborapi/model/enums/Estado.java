package org.spdgrupo.elbuensaborapi.model.enums;

public enum Estado {
    PENDIENTE_FACTURACION,
    SOLICITADO, // Este es el estado cuando ya ha sido pagado sea efectivo/mp
    EN_PREPARACION,
    TERMINADO,
    EN_CAMINO,
    ENTREGADO,
    CANCELADO
}
