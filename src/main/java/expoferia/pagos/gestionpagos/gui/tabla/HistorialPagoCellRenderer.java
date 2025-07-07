package expoferia.pagos.gestionpagos.gui.tabla;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class HistorialPagoCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        Component celda = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Si hay selección, usar color estándar
        if (isSelected) return celda;

        Object mes = table.getValueAt(row, 0); // columna 0: mes
        Object estadoObj = table.getValueAt(row, 4); // columna 4: estado

        if (mes != null && !mes.toString().isEmpty() && estadoObj != null) {
            String estado = estadoObj.toString().toLowerCase();

            switch (estado) {
                case "pendiente" -> celda.setBackground(new Color(255, 204, 204));  // rojo pastel
                case "abono"     -> celda.setBackground(new Color(255, 255, 204));  // amarillo pastel
                case "pagado"    -> celda.setBackground(new Color(204, 255, 204));  // verde pastel
                default          -> celda.setBackground(Color.WHITE);               // neutro
            }
        } else {
            celda.setBackground(Color.WHITE); // No es mensualidad, fondo blanco
        }

        return celda;
    }
}

