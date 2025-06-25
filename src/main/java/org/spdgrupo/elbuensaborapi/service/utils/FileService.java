package org.spdgrupo.elbuensaborapi.service.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.spdgrupo.elbuensaborapi.model.dto.detallefactura.DetalleFacturaResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.factura.FacturaResponseDTO;
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
        Paragraph datosEmpresa = new Paragraph("El Buen Sabor\nDirección: Calle Principal 123, Ciudad\nTeléfono: +54 123 456 7890\nCorreo: info@elbuensabor.com\n",
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
