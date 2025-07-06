package expoferia.pagos.gestionpagos.gui.tabla;

import expoferia.pagos.gestionpagos.entidades.DetallesPago;
import expoferia.pagos.gestionpagos.gui.modulos.GestorPagosEstudiante;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class HistorialPagoTableModel extends DefaultTableModel {

    private static final String[] COLUMNAS = {
            "Mes Correspondiente", "Descripción", "Tipo", "Monto Bs", "Estado"
    };

    private final List<DetallesPago> pagosOriginales = new ArrayList<>();

    public HistorialPagoTableModel() {
        super(COLUMNAS, 0);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0, 1, 2, 4 -> String.class;
            case 3 -> Double.class;
            default -> Object.class;
        };
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void cargarDesdeGestor(GestorPagosEstudiante gestor) {
        setRowCount(0);
        pagosOriginales.clear();

        for (DetallesPago pago : gestor.getTodosLosPagos()) {
            agregarFila(pago);
        }
    }

    public void cargarFiltrados(GestorPagosEstudiante gestor, String tipoFiltro) {
        setRowCount(0);
        pagosOriginales.clear();

        List<DetallesPago> filtrados = gestor.filtrarPorTipo(tipoFiltro);
        for (DetallesPago pago : filtrados) {
            agregarFila(pago);
        }
    }

    private void agregarFila(DetallesPago pago) {
        String tipo = pago.getCategoria();
        String estado;

        if (pago.tieneSaldoPendiente()) {
            estado = "Abono".equalsIgnoreCase(tipo) ? "Abono" : "Pendiente";
        } else {
            estado = "Pagado";
        }

        addRow(new Object[] {
                pago.getMesCorrespondiente(),
                pago.getDescripcion(),
                tipo,
                pago.getMontoPagado(),
                estado
        });

        pagosOriginales.add(pago);
    }

    public DetallesPago getPagoSeleccionado(int fila) {
        if (fila >= 0 && fila < pagosOriginales.size()) {
            return pagosOriginales.get(fila);
        }
        return null;
    }

    public void limpiarTabla() {
        setRowCount(0);
        pagosOriginales.clear();
    }
}
