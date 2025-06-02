package expoferia.pagos.gestionpagos.conexion;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {

    private Connection connection=null;

    public Connection getConnection() {
        final String URL="jdbc:mysql://localhost:3306/gestion_pagos";
        final String USER="root";
        final String PASSWORD="";
        if (connection==null) {
            try {
                connection= DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexión exitosa.");
            } catch (Exception e) {
                System.out.println("Error al conectar a la BD: "+e);
            }
        } else {
            System.out.println("Ya hay una conexión existente.");
        }

        return connection;
    }

    public Connection closeConnection() {
        if (connection!=null) {
            try {
                connection.close();
                System.out.println("Conexión cerrada exitosamente.");
            } catch (Exception e) {
                System.out.println("Error al cerrar la conexión a la BD: "+e);
            }
        } else {
            System.out.println("No existe conexión a cerrar.");
        }
        return connection;
    }
}
