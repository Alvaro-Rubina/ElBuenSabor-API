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
import org.spdgrupo.elbuensaborapi.model.dto.promocion.PromocionResponseDTO;
import org.spdgrupo.elbuensaborapi.model.enums.Estado;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class MercadoPagoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InsumoService.class);

    private final ProductoService productoService;
    private final InsumoService insumoService;
    private final PedidoService pedidoService;
    private final PromocionService promocionService;

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

    // Mapa temporal para guardar los PedidoDTO por ID temporal
    private static final Map<String, PedidoDTO> pedidosTemporales = new ConcurrentHashMap<>();

    @PostConstruct
    public void initMPConfig() {
        MercadoPagoConfig.setAccessToken(mpAccessToken);
    }

    public Preference createPreference(PedidoDTO pedidoDTO) throws MPException, MPApiException {

        // Generar un ID temporal único
        String tempId = UUID.randomUUID().toString();
        // Guardar el PedidoDTO temporalmente
        pedidosTemporales.put(tempId, pedidoDTO);

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
                                .email("") // No se puede obtener el email sin el pedido creado, opcional
                                .name("")  // Opcional
                                .build()
                )
                .backUrls(
                        PreferenceBackUrlsRequest.builder()
                                .success(mpSuccessBackUrl)
                                .pending(mpPendingBackUrl)
                                .failure(mpFailureBackUrl)
                                .build()
                )
                .externalReference(tempId)
                .notificationUrl(publicUrl +  "/api/mercado-pago/webhook/notification")
                .build();

        PreferenceClient client = new PreferenceClient();
        return client.create(preferenceRequest);
    }

    public boolean handlePayment(Map<String, Object> body) throws MPException, MPApiException {
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

        if (estado.equals("approved")) {
            // Recuperar el PedidoDTO temporal
            PedidoDTO pedidoDTO = pedidosTemporales.remove(externalReference);
            if (pedidoDTO == null) {
                LOGGER.error("No se encontró el PedidoDTO temporal para externalReference: " + externalReference);
                return false;
            }
            PedidoResponseDTO pedido = pedidoService.save(pedidoDTO);
            pedidoService.actualizarEstadoDelPedido(pedido.getId(), Estado.SOLICITADO);
            LOGGER.info("Pago correctamente realizado mediante Mercado Pago para pedido con id: " + pedido.getId());
        } else if (estado.equals("rejected")) {
            // Si el pago fue rechazado, simplemente eliminar el PedidoDTO temporal
            pedidosTemporales.remove(externalReference);
            LOGGER.warn("Pago rechazado por Mercado Pago para externalReference: " + externalReference);
        }

        return true;
    }

    // Adicionales - privados
    private PreferenceItemRequest getItemRequest(DetallePedidoDTO detallePedido) {
        int count = 0;
        if (detallePedido.getProductoId() != null) count++;
        if (detallePedido.getInsumoId() != null) count++;
        if (detallePedido.getPromocionId() != null) count++;

        if (count != 1) {
            throw new IllegalArgumentException("Debe especificar exactamente uno de los siguientes campos en el DetallePedido: productoId, insumoId o promocionId.");
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

        } else if (detallePedido.getInsumoId() != null) {
            InsumoResponseDTO insumo = insumoService.findById(detallePedido.getInsumoId());

            return PreferenceItemRequest.builder()
                    .id(String.valueOf(insumo.getId()))
                    .title(insumo.getDenominacion())
                    .pictureUrl(insumo.getUrlImagen())
                    .quantity(detallePedido.getCantidad())
                    .currencyId("ARS")
                    .unitPrice(BigDecimal.valueOf(insumo.getPrecioVenta()))
                    .build();
        } else {
            PromocionResponseDTO promocion = promocionService.findById(detallePedido.getPromocionId());

            return PreferenceItemRequest.builder()
                    .id(String.valueOf(promocion.getId()))
                    .title(promocion.getDescripcion())
                    .pictureUrl(promocion.getUrlImagen())
                    .quantity(detallePedido.getCantidad())
                    .currencyId("ARS")
                    .unitPrice(BigDecimal.valueOf(promocion.getPrecioVenta()))
                    .build();
        }
    }
}