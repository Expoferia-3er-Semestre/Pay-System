package expoferia.pagos.gestionpagos.gui.tabla;

import expoferia.pagos.gestionpagos.dao.DetallesPagoDAO;
import expoferia.pagos.gestionpagos.entidades.Abono;
import expoferia.pagos.gestionpagos.entidades.DetallesPago;
import expoferia.pagos.gestionpagos.gui.modulos.CarritoPago;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class DetallePagoTableModel extends DefaultTableModel {

    private static final String[] COLUMNAS = {
            "Descripción", "Cantidad", "Precio Unidad", "Total Bs"
    };
    private double montoTotal;

    private final DetallesPagoDAO detallesPagoDAO = new DetallesPagoDAO();

    public DetallePagoTableModel() {
        super(COLUMNAS, 0);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> String.class;
            case 1 -> Integer.class;
            case 2, 3 -> Double.class;
            default -> Object.class;
        };
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        // Ninguna celda editable, solo lectura
        return false;
    }

    /**
     * Carga en la tabla todos los detalles de pago asociados
     * al recibo cuyo ID se pasa como cadena.
     */
    public void cargarDatos(List<DetallesPago> conceptosPago, List<Double> montosPagados) {
        // Limpia filas previas
        setRowCount(0);

        if (conceptosPago == null) return;

        for (int i=0; i<montosPagados.size();i++) {

            DetallesPago dp = conceptosPago.get(i);
            double monto = montosPagados.get(i);
            montoTotal += montosPagados.get(i);
            addRow(new Object[]{
                    dp.getDescripcion(),
                    1,
                    monto,
                    montoTotal
            });

        }

    }

    /**
     * Suma la columna "Total Bs" y devuelve el total general.
     */
    public double getTotalGeneral() {
        double total = 0;
        for (int i = 0; i < getRowCount(); i++) {
            Object valor = getValueAt(i, 3);
            if (valor instanceof Double d) {
                total += d;
            }
        }
        return total;
    }

    public void agregarFila(DetallesPago dp, double montoPagado) {
        montoTotal += montoPagado;

        addRow(new Object[]{
                dp.getDescripcion(),
                1,
                montoPagado,
                montoTotal
        });
    }

    public void agregarFilaAbono(Abono abono, double montoPagado) {
        montoTotal += montoPagado;

        addRow(new Object[]{
                abono.getDescripcion(),
                1,
                montoPagado,
                montoTotal
        });
    }

    public void limpiarTabla() {
        setRowCount(0);
        montoTotal = 0;
    }


}