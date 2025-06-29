package org.spdgrupo.elbuensaborapi.service.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class FooterEvent extends PdfPageEventHelper {
    private final Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
    private final String footerText = "Gracias por su compra. Esperamos volver a verlo pronto.\n" +
            "Esta factura es un comprobante válido de la transacción.\n" +
            "Para consultas llame al +54 123 456 7890 o escriba a info@elbuensabor.com.";

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        float x = (document.right() + document.left()) / 2;
        float y = document.bottom() - 10;
        String[] lines = footerText.split("\n");
        float lineHeight = 12; // Ajusta si es necesario

        for (int i = 0; i < lines.length; i++) {
            ColumnText.showTextAligned(
                    writer.getDirectContent(),
                    Element.ALIGN_CENTER,
                    new Phrase(lines[i], font),
                    x,
                    y + (i * lineHeight),
                    0
            );
        }
    }
}