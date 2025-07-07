package expoferia.pagos.gestionpagos.gui;

import expoferia.pagos.gestionpagos.gui.modulos.CarritoPago;
import expoferia.pagos.gestionpagos.gui.modulos.GestorPagosEstudiante;
import expoferia.pagos.gestionpagos.gui.tabla.DetallePagoTableModel;
import expoferia.pagos.gestionpagos.gui.tabla.HistorialPagoTableModel;
import expoferia.pagos.gestionpagos.util.ConfigGeneral;

import javax.swing.table.DefaultTableModel;

public class SesionActual {

    private static DetallePagoTableModel tableModel;
    private static CarritoPago carritoPago;

    private static GestorPagosEstudiante gpe;
    private static HistorialPagoTableModel hpt;

    private static ConfigGeneral config;

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

    public static ConfigGeneral getConfigGeneral() {
        if (config == null) config = new ConfigGeneral();
        config.cargar();
        return config;
    }

}

