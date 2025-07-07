package expoferia.pagos.gestionpagos.gui.tabla;

import expoferia.pagos.gestionpagos.dao.DetallesPagoDAO;
import expoferia.pagos.gestionpagos.entidades.Abono;
import expoferia.pagos.gestionpagos.entidades.DetallesPago;
import expoferia.pagos.gestionpagos.gui.modulos.CarritoPago;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class DetallePagoTableModel extends DefaultTableModel {

    CarritoPago carritoPago;
    private final List<Object> elementosOriginales = new ArrayList<>();

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
        montoTotal = 0;

        if (conceptosPago == null) return;

        for (int i = 0; i < montosPagados.size(); i++) {
            DetallesPago dp = conceptosPago.get(i);
            double monto = montosPagados.get(i);
            montoTotal += monto;
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
        elementosOriginales.add(dp);
    }

    public void agregarFilaAbono(Abono abono, double montoPagado) {
        montoTotal += montoPagado;

        addRow(new Object[]{
                abono.getDescripcion(),
                1,
                montoPagado,
                montoTotal
        });
        elementosOriginales.add(abono);
    }

    public void limpiarTabla() {
        setRowCount(0);
        montoTotal = 0;
    }

    public Object getElementoSeleccionado(int fila) {
        if (fila >= 0 && fila < elementosOriginales.size()) {
            return elementosOriginales.get(fila);
        }
        return null;
    }

    public boolean eliminarElementoPorIdTemporal(int idTemporal) {
        for (int fila = 0; fila < elementosOriginales.size(); fila++) {
            Object elemento = elementosOriginales.get(fila);

            int idTemp = Integer.MIN_VALUE;
            if (elemento instanceof DetallesPago dp) {
                idTemp = dp.getId();
            } else if (elemento instanceof Abono ab) {
                idTemp = ab.getIdAbono();
            }

            if (idTemp == idTemporal) {
                elementosOriginales.remove(fila);
                removeRow(fila);
                recalcularMontosTotales(); // ✅ Recalcular al volar
                return true;
            }
        }
        return false;
    }

    public void recalcularMontosTotales() {
        double acumulado = 0;

        for (int fila = 0; fila < getRowCount(); fila++) {
            Object valorPrecio = getValueAt(fila, 2); // columna 2: Precio Unidad

            if (valorPrecio instanceof Double precio) {
                acumulado += precio;
                setValueAt(acumulado, fila, 3); // columna 3: Total Bs
            }
        }

        montoTotal = acumulado;
    }


}