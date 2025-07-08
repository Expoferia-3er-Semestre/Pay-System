package expoferia.pagos.gestionpagos.gui.tabla;

import expoferia.pagos.gestionpagos.entidades.DetallesPago;
import expoferia.pagos.gestionpagos.gui.modulos.GestorPagosEstudiante;

import javax.swing.table.DefaultTableModel;
import java.util.*;

public class HistorialPagoTableModel extends DefaultTableModel {

    private static final String[] COLUMNAS = {
            "Mes Correspondiente", "Descripción", "Tipo", "Monto Bs", "Estado"
    };

    private final List<DetallesPago> pagosOriginales = new ArrayList<>();
    List<DetallesPago> filtrados = new ArrayList<>();


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

    public void agregarMensualidadRow(DetallesPago detallesPago, String mes) {

        if (detallesPago != null) {

            String tipo = detallesPago.getCategoria();
            String estado;
            if (detallesPago.tieneSaldoPendiente()) {
                estado = (detallesPago.getDiferencia() > 0) ? "Abono" : "Pendiente";
            } else {
                estado = "Pagado";
            }

            addRow(new Object[] {
                    mes,
                    (detallesPago.getDescripcion() != null) ? detallesPago.getDescripcion() : "",
                    tipo,
                    detallesPago.getMontoPagado(),
                    estado
            });

        } else {

            addRow(new Object[] {
                    mes,
                    "",
                    "",
                    0,
                    "Pendiente"
            });

        }


    }

    public void cargarFiltrados(GestorPagosEstudiante gestor, String tipoFiltro) {
        setRowCount(0);
        filtrados = gestor.filtrarPorTipo(tipoFiltro);

        if (tipoFiltro.equals("Mensualidad")) {

            final List<String> MESES_ESCOLARES = Arrays.asList(
                    "Septiembre", "Octubre", "Noviembre", "Diciembre",
                    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto"
            );

            for (int i = 0; i < MESES_ESCOLARES.size(); i++) {
                String mes = MESES_ESCOLARES.get(i);

                DetallesPago pago;
                if (i < filtrados.size()) {
                    pago = filtrados.get(i);
                    System.out.println(pago.getDescripcion());
                } else pago = null;

                agregarMensualidadRow(pago, mes);
            }

        } else {
            for (DetallesPago pago : filtrados) {
                agregarFila(pago);
            }
        }

    }

    private void agregarFila(DetallesPago pago) {
        String tipo = pago.getCategoria();
        String estado;

        if (pago.tieneSaldoPendiente()) {
            estado = (pago.getDiferencia() > 0) ? "Abono" : "Pendiente";
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
