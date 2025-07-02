package expoferia.pagos.gestionpagos.gui;

import expoferia.pagos.gestionpagos.gui.modulos.CarritoPago;
import expoferia.pagos.gestionpagos.gui.tabla.DetallePagoTableModel;

import javax.swing.table.DefaultTableModel;

public class SesionActual {

    private static DetallePagoTableModel tableModel;
    private static CarritoPago carritoPago;

    public static CarritoPago getCarritoPago() {
        if (carritoPago == null) carritoPago = new CarritoPago();
        return carritoPago;
    }

    public static DetallePagoTableModel getTableModel() {
        if (tableModel == null) tableModel = new DetallePagoTableModel();
        return tableModel;
    }

    public static void reiniciarCarrito() {
        carritoPago = new CarritoPago();
    }
}

