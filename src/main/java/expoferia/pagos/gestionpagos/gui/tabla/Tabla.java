/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package expoferia.pagos.gestionpagos.gui.tabla;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class Tabla extends JTable {

    public Tabla() {
        // Configurar el renderizador de encabezado
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        headerRenderer.setBackground(Color.WHITE);
        headerRenderer.setForeground(Color.BLACK);
        setRowHeight(30);

        // Aplicarlo al encabezado de la tabla
        getTableHeader().setDefaultRenderer(headerRenderer);
        setAutoCreateRowSorter(true);
        setDefaultRenderer(Object.class, new DefaultTable());// Activar ordenamiento de columnas
    }

}
