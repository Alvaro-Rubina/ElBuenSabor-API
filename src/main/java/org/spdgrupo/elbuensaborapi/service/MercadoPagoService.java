package org.spdgrupo.elbuensaborapi.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spdgrupo.elbuensaborapi.model.dto.detallepedido.DetallePedidoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.pedido.PedidoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.pedido.PedidoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.enums.Estado;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class    MercadoPagoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InsumoService.class);

    private final ProductoService productoService;
    private final InsumoService insumoService;
    private final PedidoService pedidoService;

    @Value("${mercadopago.access_token}")
    private String mpAccessToken;

    @Value("${mercadopago.back_url.success}")
    private String mpSuccessBackUrl;

    @Value("${mercadopago.back_url.pending}")
    private String mpPendingBackUrl;

    @Value("${mercadopago.back_url.failure}")
    private String mpFailureBackUrl;

    @Value("${api.url}")
    private String publicUrl;

    @PostConstruct
    public void initMPConfig() {
        MercadoPagoConfig.setAccessToken(mpAccessToken);
    }

    // TODO: tendria que ver alguna forma de que la preferencia tenga algun tiempo de vida util (???)
    public Preference createPreference(PedidoDTO pedidoDTO) throws MPException, MPApiException {

        PedidoResponseDTO pedidoResponseDTO = pedidoService.save(pedidoDTO);
        String idPedidoString = pedidoResponseDTO.getId().toString();

        List<PreferenceItemRequest> items = new ArrayList<>();
        for (DetallePedidoDTO detallePedido: pedidoDTO.getDetallePedidos()) {
            PreferenceItemRequest itemRequest = getItemRequest(detallePedido);
            items.add(itemRequest);
        }

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .autoReturn("approved")
                .payer(
                        PreferencePayerRequest.builder()
                                .email(pedidoResponseDTO.getCliente().getUsuario().getEmail())
                                .name(pedidoResponseDTO.getCliente().getNombreCompleto())
                                .build()
                )
                .backUrls(
                        PreferenceBackUrlsRequest.builder()
                                .success(mpSuccessBackUrl + "?codigoPedido=" + pedidoResponseDTO.getCodigo())
                                .pending(mpPendingBackUrl  +"?codigoPedido=" + pedidoResponseDTO.getCodigo())
                                .failure(mpFailureBackUrl  +"?codigoPedido=" + pedidoResponseDTO.getCodigo())
                                .build()
                )
                .externalReference(idPedidoString)
                .notificationUrl(publicUrl +  "/mercado-pago/webhook/notification")
                .build();

        PreferenceClient client = new PreferenceClient();
        return client.create(preferenceRequest);
    }

    public boolean handlePayment(Map<String, Object> body) throws MPException, MPApiException {
        
        // validaciones de la notificacion
        String type = String.valueOf(body.get("type"));
        Map<String, Object> data = (Map<String, Object>) body.get("data");
        Long paymentId;

        if (type.equals("payment") && (data != null && data.get("id") != null)) {
            paymentId = Long.valueOf((String) data.get("id"));
        } else {
            return false;
        }

        PaymentClient cliente = new PaymentClient();
        Payment pago = cliente.get(paymentId);
        String estado = pago.getStatus();
        String externalReference = pago.getExternalReference();

        PedidoResponseDTO pedido = pedidoService.findById(Long.parseLong(externalReference));

        if (estado.equals("approved")) {
            pedidoService.actualizarEstadoDelPedido(pedido.getId(), Estado.SOLICITADO);
            LOGGER.info("Pago correctamente realizado mediante Mercado Pago para pedido con id: " + pedido.getId());

        } else if (estado.equals("rejected")) {
            pedidoService.actualizarEstadoDelPedido(pedido.getId(), Estado.CANCELADO);
            LOGGER.warn("Pago rechazado por Mercado Pago para pedido con id: " + pedido.getId());
        }

        return true;
    }

    // Adicionales - privados
    private PreferenceItemRequest getItemRequest(DetallePedidoDTO detallePedido) {

        if ((detallePedido.getProductoId() == null && detallePedido.getInsumoId() == null) ||
                (detallePedido.getProductoId() != null && detallePedido.getInsumoId() != null)) {
            throw new IllegalArgumentException("Debe haber un producto o un insumo en el DetallePedido, no pueden ser ambos nulos ni tampoco pueden estar ambos");
        }

        if (detallePedido.getProductoId() != null) {
            ProductoResponseDTO producto = productoService.findById(detallePedido.getProductoId());

            return PreferenceItemRequest.builder()
                    .id(String.valueOf(producto.getId()))
                    .title(producto.getDenominacion())
                    .description(producto.getDescripcion())
                    .pictureUrl(producto.getUrlImagen())
                    .quantity(detallePedido.getCantidad())
                    .currencyId("ARS")
                    .unitPrice(BigDecimal.valueOf(producto.getPrecioVenta()))
                    .build();

        } else {
            InsumoResponseDTO insumo = insumoService.findById(detallePedido.getInsumoId());

            return PreferenceItemRequest.builder()
                    .id(String.valueOf(insumo.getId()))
                    .title(insumo.getDenominacion())
                    .pictureUrl(insumo.getUrlImagen())
                    .quantity(detallePedido.getCantidad())
                    .currencyId("ARS")
                    .unitPrice(BigDecimal.valueOf(insumo.getPrecioVenta()))
                    .build();
        }
    }

}


