package expoferia.pagos.gestionpagos.gui;

import expoferia.pagos.gestionpagos.gui.modulos.CarritoPago;
import expoferia.pagos.gestionpagos.gui.modulos.GestorPagosEstudiante;
import expoferia.pagos.gestionpagos.gui.tabla.DetallePagoTableModel;
import expoferia.pagos.gestionpagos.gui.tabla.HistorialPagoTableModel;

import javax.swing.table.DefaultTableModel;

public class SesionActual {

    private static DetallePagoTableModel tableModel;
    private static CarritoPago carritoPago;

    private static GestorPagosEstudiante gpe;
    private static HistorialPagoTableModel hpt;

    public static CarritoPago getCarritoPago() {
        if (carritoPago == null) carritoPago = new CarritoPago();
        return carritoPago;
    }

    public static DetallePagoTableModel getTableModel() {
        if (tableModel == null) tableModel = new DetallePagoTableModel();
        return tableModel;
    }

    public static GestorPagosEstudiante getGestorPagos() {
        if (gpe == null) gpe = new GestorPagosEstudiante();
        return gpe;
    }

    public static HistorialPagoTableModel getHistorialModelo() {
        if (hpt == null) hpt = new HistorialPagoTableModel();
        return hpt;
    }

}

