/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expoferia.pagos.gestionpagos.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Suglin
 */
public class Variado {

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
