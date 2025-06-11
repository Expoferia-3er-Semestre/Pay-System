package expoferia.pagos.gestionpagos.gui.panelcambiante.componentes;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class DefaultTable extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (isSelected) {
            cell.setBackground(new Color(70, 130, 180)); // Color de selección
            cell.setForeground(Color.WHITE);
        } else {
            cell.setBackground(row % 2 == 0 ? new Color(230, 240, 250) : Color.WHITE);
        }

        setHorizontalAlignment(CENTER); // Centrar texto
        return cell;
    }
}
