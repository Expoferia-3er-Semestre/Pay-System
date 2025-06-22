package expoferia.pagos.gestionpagos.dao;

import expoferia.pagos.gestionpagos.entidades.DetallesPago;
import static expoferia.pagos.gestionpagos.conexion.Conexion.*;

import java.sql.*;
import java.util.ArrayList;

public class DetallesPagoDAO {

    public boolean agregar(DetallesPago detalle) {
        String sql = "INSERT INTO detalles_pago(id_ano_escolar, id_pago, id_tipo_pago, mes_correspondiente, descripcion, abono, monto_abonado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, detalle.getIdAnoEscolar());
            ps.setString(2, detalle.getIdPago());
            ps.setInt(3, detalle.getIdTipoPago());
            ps.setString(4, detalle.getMesCorrespondiente());
            ps.setString(5, detalle.getDescripcion());
            ps.setDouble(6, detalle.getAbono());
            ps.setDouble(7, detalle.getMontoAbonado());

            int filas = ps.executeUpdate();
            closeConnection();
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar detalle de pago: " + e);
            closeConnection();
            return false;
        }
    }

    public ArrayList<DetallesPago> listarPorPago(String idPago) {
        ArrayList<DetallesPago> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalles_pago WHERE id_pago = ? ORDER BY mes_correspondiente ASC";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, idPago);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetallesPago detalle = new DetallesPago(
                            rs.getInt("id_detalles_pago"),
                            rs.getInt("id_ano_escolar"),
                            rs.getString("id_pago"),
                            rs.getInt("id_tipo_pago"),
                            rs.getString("mes_correspondiente"),
                            rs.getString("descripcion"),
                            rs.getDouble("abono"),
                            rs.getDouble("monto_abonado")
                    );
                    lista.add(detalle);
                }
                closeConnection();
                return lista;
            }

        } catch (SQLException e) {
            System.out.println("Error al listar detalles de pago: " + e);
            closeConnection();
            return null;
        }
    }
}

