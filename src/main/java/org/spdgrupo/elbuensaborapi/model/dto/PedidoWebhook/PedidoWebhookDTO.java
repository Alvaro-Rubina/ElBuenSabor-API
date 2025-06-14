package org.spdgrupo.elbuensaborapi.model.dto.PedidoWebhook;

public class PedidoWebhookDTO {
    private Long pedidoId;
    private String codigoPedido;
    private String nuevoEstado; // O usa un enum si prefieres
    private String origen; // opcional: para identificar la fuente del webhook

    // getters y setters

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getCodigoPedido() {
        return codigoPedido;
    }

    public void setCodigoPedido(String codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

    public String getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(String nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }
}
