package org.spdgrupo.elbuensaborapi.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import jakarta.annotation.PostConstruct;
import org.spdgrupo.elbuensaborapi.model.dto.detallepedido.DetallePedidoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.pedido.PedidoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class    MercadoPagoService {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private InsumoService insumoService;

    @Value("${mercadopago.access_token}")
    private String mpAccessToken;

    @Value("${mercadopago.back_url.success}")
    private String mpSuccessBackUrl;

    @Value("${mercadopago.back_url.pending}")
    private String mpPendingBackUrl;

    @Value("${mercadopago.back_url.failure}")
    private String mpFailureBackUrl;

    @PostConstruct
    public void initMPConfig() {
        MercadoPagoConfig.setAccessToken(mpAccessToken);
    }

    public Preference createPreference(PedidoDTO pedido, String codigoPedido) throws MPException, MPApiException {

        List<PreferenceItemRequest> items = new ArrayList<>();

        for (DetallePedidoDTO detallePedido: pedido.getDetallePedidos()) {
            PreferenceItemRequest itemRequest = getItemRequest(detallePedido);
            items.add(itemRequest);
        }

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .autoReturn("approved")
                .backUrls(
                        PreferenceBackUrlsRequest.builder()
                                .success(mpSuccessBackUrl)
                                .pending(mpPendingBackUrl)
                                .failure(mpFailureBackUrl)
                                .build()
                )
                .externalReference(codigoPedido)
                .build();

        PreferenceClient client = new PreferenceClient();
        return client.create(preferenceRequest);
    }

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


