package expoferia.pagos.gestionpagos.dao;

import expoferia.pagos.gestionpagos.entidades.Pago;
import static expoferia.pagos.gestionpagos.conexion.Conexion.*;

        import java.sql.*;
        import java.util.ArrayList;

public class PagoDAO {

    public boolean agregar(Pago pago) {
        String sql = "INSERT INTO pago(id, id_estudiante, fecha, monto_total, estado, metodo_pago) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, pago.getId());
            ps.setInt(2, pago.getIdEstudiante());
            ps.setDate(3, pago.getFecha());
            ps.setDouble(4, pago.getMontoTotal());
            ps.setString(5, pago.getEstado()); // ← cadena, no booleano
            ps.setString(6, pago.getMetodoPago());

            int filas = ps.executeUpdate();
            closeConnection();
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar pago: " + e);
            closeConnection();
            return false;
        }
    }

    public ArrayList<Pago> listarPorEstudiante(int idEstudiante) {
        ArrayList<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM pago WHERE id_estudiante = ? ORDER BY fecha DESC";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEstudiante);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pago pago = new Pago(
                            rs.getInt("id"),
                            rs.getInt("id_estudiante"),
                            rs.getDate("fecha"),
                            rs.getDouble("monto_total"),
                            rs.getString("estado"),
                            rs.getString("metodo_pago")
                    );
                    lista.add(pago);
                }
                closeConnection();
                return lista;
            }

        } catch (SQLException e) {
            System.out.println("Error al listar pagos: " + e);
            closeConnection();
            return null;
        }
    }

    public Pago buscarPorId(int id) {
        String sql = "SELECT * FROM pago WHERE id = ?";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Pago(
                            rs.getInt("id"),
                            rs.getInt("id_estudiante"),
                            rs.getDate("fecha"),
                            rs.getDouble("monto_total"),
                            rs.getString("estado"),
                            rs.getString("metodo_pago")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar pago por ID: " + e);
        } finally {
            closeConnection();
        }

        return null;
    }
}
