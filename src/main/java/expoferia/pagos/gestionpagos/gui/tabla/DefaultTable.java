package expoferia.pagos.gestionpagos.gui.tabla;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class DefaultTable extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setHorizontalAlignment(CENTER);

        // Obtener el valor de la columna "Estado" (última columna)
        int modelRow = table.convertRowIndexToModel(row);
        int estadoCol = table.getColumnCount() - 1;
        Object estado = table.getModel().getValueAt(modelRow, estadoCol);

        boolean activo = false;
        if (estado instanceof String texto) {
            activo = texto.equalsIgnoreCase("Activo");
        }

        if (isSelected) {
            cell.setBackground(new Color(70, 130, 180));
            cell.setForeground(Color.WHITE);
        } else if (!activo) {
            cell.setBackground(new Color(255, 204, 204)); // 🔴 Rojo pastel
            cell.setForeground(Color.BLACK);
        } else {
            cell.setBackground(modelRow % 2 == 0 ? new Color(230, 240, 250) : Color.WHITE);
            cell.setForeground(Color.BLACK);
        }

        return cell;
    }
}


