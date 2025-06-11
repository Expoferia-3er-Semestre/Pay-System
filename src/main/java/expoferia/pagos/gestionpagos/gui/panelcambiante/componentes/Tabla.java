/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package expoferia.pagos.gestionpagos.gui.panelcambiante.componentes;

import expoferia.pagos.gestionpagos.entidades.Representante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;

import static expoferia.pagos.gestionpagos.conexion.Conexion.getConexion;

public class Tabla extends JTable {
    private DefaultTableModel modelo;
    Connection con=getConexion();

    public Tabla(String nombreTabla) {
        List<String>lista=obtenerNombresColumnas(nombreTabla, con);
        modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(lista.toArray());
        cargarDatos(nombreTabla, con);
        setModel(modelo);
        setAutoCreateRowSorter(true); // Activar ordenamiento de columnas
    }


    private void cargarDatos(String nombreTabla, Connection conexion) {
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + nombreTabla)) {

            while (rs.next()) {
                Object[] fila = new Object[modelo.getColumnCount()];
                for (int i = 0; i < modelo.getColumnCount(); i++) {
                    fila[i] = rs.getObject(i + 1); // Obtener valores dinámicamente
                }
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> obtenerNombresColumnas(String nombreTabla, Connection conexion) {
        List<String> nombresColumnas = new ArrayList<>();

        try {
            DatabaseMetaData metaData = conexion.getMetaData();
            ResultSet resultado = metaData.getColumns(null, null, nombreTabla, null);

            while (resultado.next()) {
                nombresColumnas.add(resultado.getString("COLUMN_NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nombresColumnas;
    }

}
