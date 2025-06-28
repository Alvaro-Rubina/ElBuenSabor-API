package org.spdgrupo.elbuensaborapi.service.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.spdgrupo.elbuensaborapi.model.dto.detallefactura.DetalleFacturaResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detallepedido.DetallePedidoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.factura.FacturaResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.pedido.PedidoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.enums.FormaPago;
import org.spdgrupo.elbuensaborapi.model.enums.TipoEnvio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FileService {

    public static byte[] getFacturaPdf(FacturaResponseDTO factura) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        // LOGO E INFORMACIÓN DE LA EMPRESA
        Image logo = Image.getInstance("src/main/resources/img/logo.png");
        logo.scaleToFit(80, 80);
        logo.setAlignment(Element.ALIGN_LEFT);
        document.add(logo);

        // Datos de la empresa en el encabezado
        Paragraph datosEmpresa = new Paragraph("El Buen Sabor\nDirección: Av. San Martin 123, Mendoza\nTeléfono: +54 123 456 7890\nCorreo: info@elbuensabor.com\n",
                FontFactory.getFont(FontFactory.HELVETICA, 10));
        datosEmpresa.setAlignment(Element.ALIGN_RIGHT);
        document.add(datosEmpresa);

        // Título
        Paragraph titulo = new Paragraph("Factura de compra", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        document.add(new Paragraph(" "));

        // INFORMACIÓN DEL PEDIDO
        TipoEnvio tipoEnvio = factura.getPedido().getTipoEnvio(); // Obtener el tipo de envío del pedido.
        FormaPago formaPago = factura.getPedido().getFormaPago();
        Paragraph infoPedido = new Paragraph("Información del Pedido:\n" +
                "Fecha de Facturación: " + factura.getFechaFacturacion() + "\n" +
                "Tipo de Envío: " + (tipoEnvio == TipoEnvio.RETIRO_LOCAL ? "Retiro en Local" : "Delivery") + "\n" +
                "Método de Pago: " + (formaPago == FormaPago.MERCADO_PAGO ? "Mercado Pago" : "Efectivo") + "\n",
                FontFactory.getFont(FontFactory.HELVETICA, 12));
        infoPedido.setAlignment(Element.ALIGN_LEFT);
        document.add(infoPedido);

        document.add(new Paragraph(" "));

        // DATOS DEL CLIENTE
        Paragraph datosCliente = new Paragraph("Datos del Cliente:\n" +
                "Nombre: " + factura.getCliente().getNombreCompleto() + "\n" +
                (tipoEnvio == TipoEnvio.DELIVERY ? "Dirección: " + factura.getPedido().getDomicilio() + "\n" : "") + // Mostrar dirección solo si es DELIVERY.
                "Contacto: " + factura.getCliente().getTelefono() + "\n",
                FontFactory.getFont(FontFactory.HELVETICA, 12));
        datosCliente.setAlignment(Element.ALIGN_LEFT);
        document.add(datosCliente);

        // Espaciado entre secciones
        document.add(new Paragraph(" "));

        // TABLA DE PRODUCTOS
        PdfPTable table = new PdfPTable(4); // Producto, Cantidad, Unitario, Subtotal
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        table.setWidths(new float[]{2f, 1f, 1f, 1f});

        // Encabezado de la tabla
        addTableHeader(table, "Producto", "Cantidad", "Precio Unitario", "Subtotal");

        // Contenido de la tabla
        for (DetalleFacturaResponseDTO detalle : factura.getDetalleFacturas()) {
            String descripcion;
            if (detalle.getProducto() != null) {
                descripcion = detalle.getProducto().getDenominacion();
            } else if (detalle.getInsumo() != null) {
                descripcion = detalle.getInsumo().getDenominacion();
            } else if (detalle.getPromocion() != null) {
                descripcion = "Promoción: " + detalle.getPromocion().getDenominacion();
            } else {
                descripcion = "";
            }

            table.addCell(descripcion);
            table.addCell(String.valueOf(detalle.getCantidad()));
            table.addCell(String.format("$ %.2f", detalle.getSubTotal() / detalle.getCantidad()));
            table.addCell(String.format("$ %.2f", detalle.getSubTotal()));
        }

        document.add(table);

        // TOTALES
        Paragraph totales = new Paragraph(
                "Costos:\n" +
                        "Subtotal: $" + (factura.getTotalVenta() - factura.getCostoEnvio()) + "\n" +
                        "Costo de Envío: $" + factura.getCostoEnvio() + "\n" +
                        "Total: $" + factura.getTotalVenta(),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
        totales.setAlignment(Element.ALIGN_RIGHT);
        document.add(totales);

        // TÉRMINOS Y CONDICIONES
        Paragraph pie = new Paragraph("\n\nGracias por su compra. Esperamos volver a verlo pronto.\n" +
                "Esta factura es un comprobante válido de la transacción.\n" +
                "Para consultas llame al +54 123 456 7890 o escriba a info@elbuensabor.com.\n",
                FontFactory.getFont(FontFactory.HELVETICA, 10));
        pie.setAlignment(Element.ALIGN_CENTER);
        document.add(pie);

        document.close();
        return baos.toByteArray();
    }

    public static byte[] getPedidoPdf(PedidoResponseDTO pedido) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        document.open();

        // Logo e información de la empresa
        Image logo = Image.getInstance("src/main/resources/img/logo.png");
        logo.scaleToFit(80, 80);
        logo.setAlignment(Element.ALIGN_LEFT);
        document.add(logo);

        Paragraph datosEmpresa = new Paragraph("El Buen Sabor\nDirección: Av. San Martin 123, Mendoza\nTeléfono: +54 123 456 7890\nCorreo: info@elbuensabor.com\n",
                FontFactory.getFont(FontFactory.HELVETICA, 10));
        datosEmpresa.setAlignment(Element.ALIGN_RIGHT);
        document.add(datosEmpresa);

        // Título
        Paragraph titulo = new Paragraph("Resumen de Pedido", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        document.add(new Paragraph(" "));

        // Información del pedido
        Paragraph infoPedido = new Paragraph("Datos del Pedido:\n" +
                "Fecha: " + pedido.getFecha() + "\n" +
                "Hora: " + pedido.getHora() + "\n" +
                "Código: " + pedido.getCodigo() + "\n" +
                "Tipo de Envío: " + (pedido.getTipoEnvio() == org.spdgrupo.elbuensaborapi.model.enums.TipoEnvio.RETIRO_LOCAL ? "Retiro en Local" : "Delivery") + "\n" +
                "Estado: " + pedido.getEstado() + "\n" +
                "Comentario: " + (pedido.getComentario() != null ? pedido.getComentario() : "-") + "\n",
                FontFactory.getFont(FontFactory.HELVETICA, 12));
        infoPedido.setAlignment(Element.ALIGN_LEFT);
        document.add(infoPedido);

        document.add(new Paragraph(" "));

        // Datos del cliente
        Paragraph datosCliente = new Paragraph("Cliente:\n" +
                "Nombre: " + pedido.getCliente().getNombreCompleto() + "\n" +
                "Teléfono: " + pedido.getCliente().getTelefono() + "\n" +
                "Correo electrónico: " + pedido.getCliente().getUsuario().getEmail(),
                FontFactory.getFont(FontFactory.HELVETICA, 12));
        datosCliente.setAlignment(Element.ALIGN_LEFT);
        document.add(datosCliente);

        if (pedido.getTipoEnvio() == org.spdgrupo.elbuensaborapi.model.enums.TipoEnvio.DELIVERY && pedido.getDomicilio() != null) {
            document.add(new Paragraph("Dirección de entrega: " + pedido.getDomicilio() + "\n",
                    FontFactory.getFont(FontFactory.HELVETICA, 12)));
        }

        document.add(new Paragraph(" "));

        // Tabla de productos/insumos/promociones
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        table.setWidths(new float[]{2.5f, 1f, 1f, 1f, 1f, 1f});

        addTableHeader(table, "Descripción", "Cantidad", "Precio Costo", "Precio Unitario", "Subtotal Costo", "Subtotal");

        for (DetallePedidoResponseDTO detalle : pedido.getDetallePedidos()) {
            String descripcion;
            double precioUnitario = 0.0;
            double precioCosto = 0.0;

            if (detalle.getProducto() != null) {
                descripcion = detalle.getProducto().getDenominacion();
                precioUnitario = detalle.getProducto().getPrecioVenta();
                precioCosto = detalle.getProducto().getPrecioCosto();

            } else if (detalle.getInsumo() != null) {
                descripcion = detalle.getInsumo().getDenominacion();
                precioUnitario = detalle.getInsumo().getPrecioVenta();
                precioCosto = detalle.getInsumo().getPrecioCosto();

            } else if (detalle.getPromocion() != null) {
                descripcion = "Promoción: " + detalle.getPromocion().getDenominacion();
                if (detalle.getPromocion().getDescripcion() != null && !detalle.getPromocion().getDescripcion().isEmpty()) {
                    descripcion += " - (" + detalle.getPromocion().getDescripcion() + ")";
                }
                precioUnitario = detalle.getPromocion().getPrecioVenta();
                precioCosto = detalle.getPromocion().getPrecioCosto();

            } else {
                descripcion = "";
            }

            table.addCell(descripcion);
            table.addCell(String.valueOf(detalle.getCantidad()));
            table.addCell(String.format("$ %.2f", precioCosto));
            table.addCell(String.format("$ %.2f", precioUnitario));
            table.addCell(String.format("$ %.2f", precioCosto * detalle.getCantidad()));
            table.addCell(String.format("$ %.2f", detalle.getSubTotal()));
        }
        document.add(table);

        // Tabla de totales
        PdfPTable totalesTable = new PdfPTable(2);
        totalesTable.setWidthPercentage(100);
        totalesTable.setSpacingBefore(10f);
        totalesTable.setWidths(new float[]{2f, 1f});

        addTableHeader(totalesTable, "Concepto", "Monto");

        totalesTable.addCell("Costo Total");
        totalesTable.addCell(String.format("$ %.2f", pedido.getTotalCosto()));

        totalesTable.addCell("Subtotal");
        totalesTable.addCell(String.format("$ %.2f", pedido.getTotalVenta() - pedido.getCostoEnvio()));

        totalesTable.addCell("Costo de Envío");
        totalesTable.addCell(String.format("$ %.2f", pedido.getCostoEnvio()));

        PdfPCell totalCell = new PdfPCell(new Phrase("Total", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        totalesTable.addCell(totalCell);

        PdfPCell totalMontoCell = new PdfPCell(new Phrase(String.format("$ %.2f", pedido.getTotalVenta()), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        totalesTable.addCell(totalMontoCell);

        document.add(totalesTable);

        document.close();
        return baos.toByteArray();
    }

    private static void addTableHeader(PdfPTable table, String... columnHeaders) {
        for (String header : columnHeaders) {
            PdfPCell headerCell = new PdfPCell();
            headerCell.setPhrase(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(headerCell);
        }
    }
}
