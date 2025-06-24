package expoferia.pagos.gestionpagos.dao;

import expoferia.pagos.gestionpagos.entidades.DetallesPago;
import expoferia.pagos.gestionpagos.entidades.Abono;
import static expoferia.pagos.gestionpagos.conexion.Conexion.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetallesPagoDAO {

    public boolean agregar(DetallesPago detalle) {
        String sql = "INSERT INTO detalles_pago(id_ano_escolar, id_pago, id_tipo_pago, mes_correspondiente, descripcion) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, detalle.getIdAnoEscolar());
            ps.setString(2, detalle.getIdPago());
            ps.setInt(3, detalle.getIdTipoPago());
            ps.setString(4, detalle.getMesCorrespondiente());
            ps.setString(5, detalle.getDescripcion());

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
        String sql = """
            SELECT dp.*, tp.costo AS monto_esperado
            FROM detalles_pago dp
            JOIN tipo_pago tp ON dp.id_tipo_pago = tp.id_tipo_pago
            WHERE dp.id_pago = ?
            ORDER BY dp.mes_correspondiente ASC
        """;

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
                            rs.getString("descripcion")
                    );

                    detalle.setMontoEsperado(rs.getDouble("monto_esperado"));
                    detalle.setAbonos(obtenerAbonosPorDetalle(detalle.getIdDetallesPago()));

                    lista.add(detalle);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al listar detalles de pago: " + e);
            closeConnection();
            return null;
        }

        return lista;
    }

    public List<Abono> obtenerAbonosPorDetalle(int idDetallePago) {
        List<Abono> abonos = new ArrayList<>();
        String sql = "SELECT * FROM abono WHERE id_detalles_pago = ? ORDER BY fecha_abono ASC";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idDetallePago);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Abono a = new Abono(
                            rs.getInt("id_abono"),
                            rs.getInt("id_detalles_pago"),
                            rs.getDate("fecha_abono").toLocalDate(),
                            rs.getDouble("monto_abonado"),
                            rs.getString("descripcion")
                    );
                    abonos.add(a);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener abonos: " + e);
        }

        return abonos;
    }
}

