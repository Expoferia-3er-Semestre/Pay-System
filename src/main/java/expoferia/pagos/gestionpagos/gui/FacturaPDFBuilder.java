package expoferia.pagos.gestionpagos.gui;
import expoferia.pagos.gestionpagos.entidades.DetallesPago;
import expoferia.pagos.gestionpagos.entidades.PagoRecibo;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class FacturaPDFBuilder {

    public static void generarFactura(PagoRecibo recibo, List<DetallesPago> detalles, String rutaArchivoPDF, String rutaLogo) throws IOException {
        PDDocument documento = new PDDocument();
        PDPage pagina = new PDPage(PDRectangle.LETTER);
        documento.addPage(pagina);

        PDPageContentStream contenido = new PDPageContentStream(documento, pagina);

        // 🖼️ Logo institucional
        if (rutaLogo != null && !rutaLogo.isBlank()) {
            try {
                PDImageXObject logo = PDImageXObject.createFromFile(rutaLogo, documento);
                contenido.drawImage(logo, 50, 700, 100, 100); // posición y tamaño
            } catch (IOException e) {
                System.out.println("No se pudo insertar el logo: " + e.getMessage());
            }
        }

        // 🏫 Encabezado del recibo
        contenido.setFont(PDType1Font.HELVETICA_BOLD, 16);
        contenido.beginText();
        contenido.newLineAtOffset(200, 740);
        contenido.showText("Colegio Gonzaga");
        contenido.newLineAtOffset(0, -20);
        contenido.setFont(PDType1Font.HELVETICA, 12);
        contenido.showText("Recibo de Pago");
        contenido.newLineAtOffset(0, -20);
        String fecha = new SimpleDateFormat("dd/MM/yyyy").format(recibo.getFechaPago());
        contenido.showText("Fecha: " + fecha);
        contenido.endText();

        // 👨‍👧 Datos del estudiante
        contenido.setFont(PDType1Font.HELVETICA, 11);
        contenido.beginText();
        contenido.newLineAtOffset(50, 660);
        contenido.showText("Estudiante ID: " + recibo.getIdEstudiante());
        contenido.newLineAtOffset(0, -15);
        contenido.showText("Monto Total: " + recibo.getMontoTotal() + " Bs");
        contenido.newLineAtOffset(0, -15);
        contenido.showText("Monto Pagado: " + recibo.getMontoPagado() + " Bs");
        contenido.endText();

        // 📋 Tabla de conceptos
        float yInicio = 600;
        float margenIzq = 50;
        float anchoCelda = 100;
        float altoFila = 20;

        contenido.setLineWidth(0.5f);
        contenido.setFont(PDType1Font.HELVETICA_BOLD, 10);

        // Encabezado
        String[] columnas = { "Mes", "Descripción", "Método", "Transacción", "Total", "Abonado" };
        for (int i = 0; i < columnas.length; i++) {
            float x = margenIzq + i * anchoCelda;
            contenido.beginText();
            contenido.newLineAtOffset(x + 2, yInicio);
            contenido.showText(columnas[i]);
            contenido.endText();
            contenido.addRect(x, yInicio - altoFila, anchoCelda, altoFila);
        }
        contenido.stroke();

        // Filas de detalles
        contenido.setFont(PDType1Font.HELVETICA, 9);
        float yActual = yInicio - altoFila;

        for (DetallesPago dp : detalles) {
            String[] datos = {
                    dp.getMesCorrespondiente() != null ? dp.getMesCorrespondiente() : "-",
                    dp.getDescripcion(),
                    dp.getMetodoPago(),
                    dp.getNumTrans() != null ? dp.getNumTrans() : "-",
                    String.format("%.2f", dp.getMontoTotal()),
                    String.format("%.2f", dp.getMontoPagado())
            };
            for (int i = 0; i < datos.length; i++) {
                float x = margenIzq + i * anchoCelda;
                contenido.beginText();
                contenido.newLineAtOffset(x + 2, yActual);
                contenido.showText(datos[i]);
                contenido.endText();
                contenido.addRect(x, yActual - altoFila, anchoCelda, altoFila);
            }
            contenido.stroke();
            yActual -= altoFila;
        }

        // 🖋️ Firma
        contenido.beginText();
        contenido.newLineAtOffset(400, 100);
        contenido.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
        contenido.showText("Firma del Administrador:");
        contenido.endText();

        contenido.moveTo(400, 95);
        contenido.lineTo(550, 95);
        contenido.stroke();

        contenido.close();
        documento.save(rutaArchivoPDF);
        documento.close();

        System.out.println("Factura generada con éxito en: " + rutaArchivoPDF);
    }
}
