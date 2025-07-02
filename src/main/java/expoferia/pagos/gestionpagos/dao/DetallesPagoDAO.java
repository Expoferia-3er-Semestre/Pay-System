package expoferia.pagos.gestionpagos.dao;

import expoferia.pagos.gestionpagos.entidades.DetallesPago;
import expoferia.pagos.gestionpagos.entidades.Abono;
import static expoferia.pagos.gestionpagos.conexion.Conexion.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetallesPagoDAO {

    public boolean agregar(DetallesPago detalle) {
        String sql = "INSERT INTO detalles_pago(id_ano_escolar, id_pago_recibo, id_tipo_pago, mes_correspondiente, descripcion, monto_total, monto_pagado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, detalle.getIdAnoEscolar());
            ps.setInt(2, detalle.getIdPagoRecibo());
            ps.setInt(3, detalle.getIdTipoPago());
            ps.setString(4, detalle.getMesCorrespondiente());
            ps.setString(5, detalle.getDescripcion());
            ps.setDouble(6, detalle.getMontoTotal());
            ps.setDouble(7, detalle.getMontoPagado());

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar detalle de pago: " + e);
            return false;
        } finally {
            closeConnection();
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
                            rs.getInt("id_pago_recibo"),
                            rs.getInt("id_tipo_pago"),
                            rs.getString("num_trans"),
                            rs.getInt("id_ano_escolar"),
                            rs.getString("descripcion"),
                            rs.getString("mes_correspondiente"),
                            rs.getDouble("monto_total"),
                            rs.getDouble("monto_pagado"),
                            rs.getString("metodo_pago")
                            );

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

    public List<DetallesPago> listarPorEstudiante(int idEstudiante, int idAnoEscolar) {
        List<DetallesPago> lista = new ArrayList<>();

        String sql = """
    SELECT dp.*
    FROM detalles_pago dp
    JOIN pago_recibo pr ON dp.id_pago_recibo = pr.id_pago_recibo
    WHERE pr.id_estudiante = ?
      AND dp.id_ano_escolar = ?
      AND dp.mes_correspondiente IS NOT NULL
    """;


        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEstudiante);
            ps.setInt(2, idAnoEscolar);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetallesPago dp = new DetallesPago();

                    dp.setId(rs.getInt("id"));
                    dp.setIdPagoRecibo(rs.getInt("id_pago_recibo"));
                    dp.setIdTipoPago(rs.getInt("id_tipo_pago"));
                    dp.setMetodoPago(rs.getString("metodo_pago"));
                    dp.setNumTrans(rs.getString("num_trans"));
                    dp.setIdAnoEscolar(rs.getInt("id_ano_escolar"));
                    dp.setDescripcion(rs.getString("descripcion"));
                    dp.setMesCorrespondiente(rs.getString("mes_correspondiente"));
                    dp.setMontoTotal(rs.getDouble("monto_total"));
                    dp.setMontoPagado(rs.getDouble("monto_pagado"));

                    lista.add(dp);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al listar detalles por estudiante: " + e);
        } finally {
            closeConnection();
        }

        return lista;
    }


    public List<String> obtenerMesesPagados(int idEstudiante, int idAnoEscolar) {

        List<String> mesesPagados = new ArrayList<>();
        String sql = """
        SELECT dp.mes_correspondiente
        FROM detalles_pago dp
        JOIN pago_recibo pr ON dp.id_pago_recibo = pr.id_pago_recibo
        JOIN tipo_pago tp ON dp.id_tipo_pago = tp.id
        WHERE pr.id_estudiante = ?
          AND dp.id_ano_escolar = ?
          AND tp.categoria = 'Mensualidad'
    """;

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEstudiante);
            ps.setInt(2, idAnoEscolar);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String mes = rs.getString("mes_correspondiente");
                    mesesPagados.add(mes);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener meses pagados: " + e);
        } finally {
            closeConnection();
        }

        return mesesPagados;
    }

    public boolean actualizarMontoPagado(int idDetallePago, double nuevoMontoPagado) {
        String sql = "UPDATE detalles_pago SET monto_pagado = ? WHERE id_detalles_pago = ?";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, nuevoMontoPagado);
            ps.setInt(2, idDetallePago);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar monto_pagado: " + e);
            return false;
        } finally {
            closeConnection();
        }
    }

}

