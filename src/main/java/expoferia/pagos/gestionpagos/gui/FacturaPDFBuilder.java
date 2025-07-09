package expoferia.pagos.gestionpagos.gui;
import expoferia.pagos.gestionpagos.entidades.Abono;
import expoferia.pagos.gestionpagos.entidades.DetallesPago;
import expoferia.pagos.gestionpagos.entidades.Estudiante;
import expoferia.pagos.gestionpagos.entidades.PagoRecibo;
import expoferia.pagos.gestionpagos.gui.modulos.CarritoPago;
import expoferia.pagos.gestionpagos.gui.tabla.DetallePagoTableModel;
import expoferia.pagos.gestionpagos.util.ConfigGeneral;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FacturaPDFBuilder {

    public static void generarFactura(DetallePagoTableModel modelo, Estudiante estudiante, CarritoPago carrito, int idFactura) {
        try (PDDocument documento = new PDDocument()) {
            PDPage pagina = new PDPage(PDRectangle.LETTER);
            documento.addPage(pagina);

            PDPageContentStream contenido = new PDPageContentStream(documento, pagina);

            // Encabezado
            contenido.beginText();
            contenido.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contenido.newLineAtOffset(50, 720);
            contenido.showText("Instituto " + new ConfigGeneral().get("nombre_colegio"));
            contenido.endText();

            // Fecha, RIF y alumno
            contenido.beginText();
            contenido.setFont(PDType1Font.HELVETICA, 11);
            contenido.newLineAtOffset(50, 700);
            contenido.showText("Fecha: " + LocalDate.now());
            contenido.newLineAtOffset(0, -15);
            contenido.showText("RIF: " + new ConfigGeneral().get("rif"));
            contenido.newLineAtOffset(0, -15);
            contenido.showText("Alumno: " + estudiante.getNombre1() + " " + estudiante.getApellido1());
            contenido.endText();

            // Título sección detalle
            float y = 645;
            contenido.setFont(PDType1Font.HELVETICA, 10);
            contenido.beginText();
            contenido.newLineAtOffset(50, y);
            contenido.showText("Detalle de pagos:");
            contenido.endText();

            // Filas de la tabla
            float xDescripcion = 50;
            float xCantidad    = 180;
            float xPrecio      = 230;

            y -= 20;

            for (int i = 0; i < modelo.getRowCount(); i++) {
                String descripcion = modelo.getValueAt(i, 0).toString();
                int cantidad       = (Integer) modelo.getValueAt(i, 1);
                double precio      = (Double) modelo.getValueAt(i, 2);

                // Descripción
                contenido.beginText();
                contenido.newLineAtOffset(xDescripcion, y);
                contenido.showText(descripcion);
                contenido.endText();

                // Cantidad
                contenido.beginText();
                contenido.newLineAtOffset(xCantidad, y);
                contenido.showText(String.valueOf(cantidad));
                contenido.endText();

                // Precio
                contenido.beginText();
                contenido.newLineAtOffset(xPrecio, y);
                contenido.showText("Bs " + String.format("%.2f", precio));
                contenido.endText();

                y -= 15;
            }


            // Totales por método de pago (solo si hay montos > 0)
            Map<String, Double> resumen = obtenerMontosPorMetodoPago(carrito.getConceptos());

            double total = 0;
            for (Map.Entry<String, Double> entry : resumen.entrySet()) {
                if (entry.getValue() > 0) {
                    y -= 15;
                    contenido.beginText();
                    contenido.newLineAtOffset(50, y);
                    contenido.setFont(PDType1Font.HELVETICA, 10);
                    contenido.showText("Total " + entry.getKey() + ": Bs " + entry.getValue());
                    total += entry.getValue();
                    contenido.endText();
                }
            }

            // Total general
            y -= 20;
            contenido.beginText();
            contenido.newLineAtOffset(50, y);
            contenido.setFont(PDType1Font.HELVETICA_BOLD, 11);
            contenido.showText("Total: Bs " + total);
            contenido.endText();

            // Pie de factura
            y -= 40;
            contenido.beginText();
            contenido.newLineAtOffset(50, y);
            contenido.setFont(PDType1Font.HELVETICA_OBLIQUE, 9);
            contenido.showText("Gracias por su pago.");
            contenido.endText();

            contenido.close();

            // Guardar factura en carpeta Documents/Facturas
            String rutaBase = System.getProperty("user.home") + "/Documents/Facturas";
            new File(rutaBase).mkdirs(); // crea si no existe

            String nombreArchivo = rutaBase + "/Factura_" + idFactura + "_" + estudiante.getId() + "_" + LocalDate.now() + ".pdf";
            documento.save(nombreArchivo);

            System.out.println("📄 Factura guardada como: " + nombreArchivo);

        } catch (IOException e) {
            System.out.println("Error al generar factura: " + e.getMessage());
        }
    }


    public static Map<String, Double> obtenerMontosPorMetodoPago(List<DetallesPago> listaPagos) {
        Map<String, Double> totales = new LinkedHashMap<>();

        for (DetallesPago pago : listaPagos) {
            String metodo = pago.getMetodoPago();
            totales.merge(metodo, pago.getMontoPagado(), Double::sum);

            List<Abono> abonos = pago.getAbonos();
            if (abonos != null) {
                for (Abono ab : abonos) {
                    String metodoAbono = ab.getMetodoPago();
                    totales.merge(metodoAbono, ab.getMontoAbonado(), Double::sum);
                }
            }
        }

        return totales;
    }


}
