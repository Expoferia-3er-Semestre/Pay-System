package expoferia.pagos.gestionpagos.dao;

import expoferia.pagos.gestionpagos.entidades.Abono;
import static expoferia.pagos.gestionpagos.conexion.Conexion.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbonoDAO {

    public boolean agregarAbono(Abono abono) {
        String sql = "INSERT INTO abono (id_detalles_pago, fecha_abono, monto_abonado, descripcion, metodo_pago, descripcion) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, abono.getIdDetallesPago());
            ps.setDate(2, Date.valueOf(abono.getFechaAbono()));
            ps.setDouble(3, abono.getMontoAbonado());
            ps.setString(4, abono.getDescripcion());
            ps.setString(5, abono.getMetodoPago());
            ps.setString(6, abono.getNumTrans());

            int filas = ps.executeUpdate();
            closeConnection();
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar abono: " + e);
            closeConnection();
            return false;
        }
    }
}

