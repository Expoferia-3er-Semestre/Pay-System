package expoferia.pagos.gestionpagos.dao;

import expoferia.pagos.gestionpagos.entidades.Abono;
import static expoferia.pagos.gestionpagos.conexion.Conexion.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbonoDAO {

    public boolean agregarAbono(Abono abono) {
        String sql = "INSERT INTO abono (id_detalles_pago, fecha_abono, monto_abonado, descripcion) VALUES (?, ?, ?, ?)";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, abono.getIdDetallesPago());
            ps.setDate(2, Date.valueOf(abono.getFechaAbono()));
            ps.setDouble(3, abono.getMontoAbonado());
            ps.setString(4, abono.getDescripcion());

            int filas = ps.executeUpdate();
            closeConnection();
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar abono: " + e);
            closeConnection();
            return false;
        }
    }

    public List<Abono> listarPorDetalle(int idDetallesPago) {
        List<Abono> lista = new ArrayList<>();
        String sql = "SELECT * FROM abono WHERE id_detalles_pago = ? ORDER BY fecha_abono ASC";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idDetallesPago);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Abono abono = new Abono(
                            rs.getInt("id_abono"),
                            rs.getInt("id_detalles_pago"),
                            rs.getDate("fecha_abono").toLocalDate(),
                            rs.getDouble("monto_abonado"),
                            rs.getString("descripcion")
                    );
                    lista.add(abono);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al listar abonos: " + e);
        }

        return lista;
    }
}

