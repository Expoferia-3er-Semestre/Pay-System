package expoferia.pagos.gestionpagos.gui.tabla;

import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class DashboardPagoTableModel extends DefaultTableModel {

    private static final String[] COLUMNAS = {
            "ID", "Monto Total", "Monto Pagado", "Fecha", "Estado"
    };

    public DashboardPagoTableModel() {
        super(COLUMNAS, 0);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false; // 🔒 No editable desde la tabla
    }

    /**
     * Agrega una fila representando un pago recibido.
     *
     * @param id            ID del pago
     * @param montoTotal    Monto total de la transacción
     * @param montoPagado   Monto realmente pagado
     * @param fechaPago     Fecha de pago (java.util.Date o java.sql.Date)
     * @param estado        Estado lógico: true = Activo, false = Archivado
     */
    public void agregarFila(int id, double montoTotal, double montoPagado, Date fechaPago, boolean estado) {
        String textoEstado = estado ? "Pagado" : "Pendiente";

        // ✅ Formateo opcional para la fecha (si quieres mostrarla como dd/MM/yyyy)
        String fechaFormateada = new SimpleDateFormat("dd/MM/yyyy").format(fechaPago);

        addRow(new Object[]{
                id,
                String.format("Bs %.2f", montoTotal), // Formato bonito
                String.format("Bs %.2f", montoPagado),
                fechaFormateada,
                textoEstado
        });
    }

    /**
     * Limpia todas las filas de la tabla.
     */
    public void limpiar() {
        setRowCount(0); // 🧹 Adiós filas
    }
}

