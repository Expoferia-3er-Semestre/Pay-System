package expoferia.pagos.gestionpagos.dao;

import expoferia.pagos.gestionpagos.entidades.Abono;
import static expoferia.pagos.gestionpagos.conexion.Conexion.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AbonoDAO {

    public boolean agregarAbono(List<Abono> abonos) {
        if (abonos == null || abonos.isEmpty()) return false;

        String sqlInsert = """
        INSERT INTO abono 
        (id_detalles_pagos, fecha_abono, monto_abonado, descripcion, metodo_pago, num_trans)
        VALUES (?, ?, ?, ?, ?, ?)
    """;

        String sqlUpdate = """
        UPDATE detalles_pago
        SET monto_pagado = monto_pagado + ?
        WHERE id = ?
    """;

        try (Connection con = getConexion();
             PreparedStatement psInsert = con.prepareStatement(sqlInsert);
             PreparedStatement psUpdate = con.prepareStatement(sqlUpdate)) {

            con.setAutoCommit(false); // 🔒 Inicia la transacción

            for (Abono abono : abonos) {
                // Insertar abono
                psInsert.setInt(1, abono.getIdDetallesPago());
                psInsert.setDate(2, Date.valueOf(abono.getFechaAbono()));
                psInsert.setDouble(3, abono.getMontoAbonado());
                psInsert.setString(4, abono.getDescripcion());
                psInsert.setString(5, abono.getMetodoPago());
                psInsert.setString(6, abono.getNumTrans());
                psInsert.addBatch();

                // Actualizar monto_pagado del detalle
                psUpdate.setDouble(1, abono.getMontoAbonado());
                psUpdate.setInt(2, abono.getIdDetallesPago());
                psUpdate.addBatch();
            }

            psInsert.executeBatch();
            psUpdate.executeBatch();
            con.commit(); // ✅ Todo correcto, se guarda

            closeConnection();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al insertar abonos y actualizar detalles: " + e);
            try {
                getConexion().rollback(); // ❌ Algo falló, se revierte todo
            } catch (SQLException rollbackEx) {
                System.out.println("Error al hacer rollback: " + rollbackEx);
            }
            closeConnection();
            return false;
        }
    }
}

