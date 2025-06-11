/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package expoferia.pagos.gestionpagos.gui.panelcambiante.componentes;

import expoferia.pagos.gestionpagos.entidades.Representante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class Tabla extends JTable {
    private DefaultTableModel modelo;

    public Tabla() {
        modelo = new DefaultTableModel(
            new Object [][] {},
            new String [] {"ID", "Cédula", "Apellidos", "Nombres", "Teléfono", "Correo", "Fecha N", "Dirección"}
        );
        setModel(modelo);
        setAutoCreateRowSorter(true); // Activar ordenamiento de columnas

    }

    public void cargarDatos(List<Representante> listaRepresentante) {

        modelo.setRowCount(0); // Limpiar la tabla antes de actualizar datos
        for (Representante repre : listaRepresentante) {
            String nombreCompleto = repre.getNombre1() + " " + repre.getNombre2();
            String apellidoCompleto = repre.getApellido1() + " " + repre.getApellido2();
            modelo.addRow(new Object[]{repre.getId(), repre.getCedula(), apellidoCompleto,
                    nombreCompleto, repre.getTelefono(), repre.getCorreo(), repre.getFechaN(),
                    repre.getDireccion()});
        }

    }
}
