package expoferia.pagos.gestionpagos.dao;

import expoferia.pagos.gestionpagos.entidades.Abono;
import expoferia.pagos.gestionpagos.entidades.DetallesPago;
import expoferia.pagos.gestionpagos.entidades.PagoRecibo;
import static expoferia.pagos.gestionpagos.conexion.Conexion.*;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class PagoReciboDAO {

        public List<PagoRecibo> listarPorFecha(Date fecha) {
                List<PagoRecibo> lista = new ArrayList<>();
                String sql = "SELECT * FROM pago_recibo WHERE fecha_pago = ? ORDER BY fecha_pago ASC";

                try (Connection con = getConexion();
                     PreparedStatement ps = con.prepareStatement(sql)) {

                        ps.setDate(1, new java.sql.Date(fecha.getTime()));

                        try (ResultSet rs = ps.executeQuery()) {
                                while (rs.next()) {
                                        PagoRecibo pago = new PagoRecibo(
                                                rs.getInt("id_pago_recibo"),
                                                rs.getInt("id_estudiante"),
                                                rs.getDouble("monto_total"),
                                                rs.getDouble("monto_pagado"),
                                                rs.getBoolean("estado"),
                                                rs.getDate("fecha_pago")
                                        );
                                        lista.add(pago);
                                }
                        }

                } catch (Exception e) {
                        System.out.println("Error al consultar pagos por fecha: " + e.getMessage());
                } finally {
                        closeConnection();
                }

                return lista;
        }


        public Integer obtenerProximoIdPagoRecibo() {
                String sql = "SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'gestion_pagos' AND TABLE_NAME = 'pago_recibo'";

                try (Connection con = getConexion();
                     PreparedStatement ps = con.prepareStatement(sql);
                     ResultSet rs = ps.executeQuery()) {

                        if (rs.next()) {
                                return rs.getInt("AUTO_INCREMENT");
                        }

                } catch (SQLException e) {
                        System.out.println("Error al obtener próximo ID de pago_recibo: " + e.getMessage());
                } finally {
                        closeConnection();
                }

                return null; // En caso de error o que no exista
        }

        public boolean registrarPago(PagoRecibo recibo, List<DetallesPago> nuevosDetalles, List<Abono> abonos) {
                String sqlRecibo = "INSERT INTO pago_recibo (id_estudiante, monto_total, monto_pagado, estado, fecha_pago) VALUES (?, ?, ?, ?, ?)";
                String sqlDetalle = """
        INSERT INTO detalles_pago 
        (id_pago_recibo, id_tipo_pago, metodo_pago, num_trans, id_ano_escolar, descripcion, mes_correspondiente, monto_total, monto_pagado)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;
                String sqlAbono = """
        INSERT INTO abono 
        (id_detalles_pagos, fecha_abono, monto_abonado, descripcion, metodo_pago, num_trans)
        VALUES (?, ?, ?, ?, ?, ?)
    """;
                String sqlActualizarMonto = """
        UPDATE detalles_pago dp
        SET monto_pagado = (
            SELECT COALESCE(SUM(a.monto_abonado), 0)
            FROM abono a
            WHERE a.id_detalles_pagos = dp.id
        )
        WHERE dp.id = ?
    """;

                try (Connection con = getConexion();
                     PreparedStatement psRecibo = con.prepareStatement(sqlRecibo, Statement.RETURN_GENERATED_KEYS);
                     PreparedStatement psDetalle = con.prepareStatement(sqlDetalle, Statement.RETURN_GENERATED_KEYS);
                     PreparedStatement psAbono = con.prepareStatement(sqlAbono);
                     PreparedStatement psUpdateMonto = con.prepareStatement(sqlActualizarMonto)) {

                        con.setAutoCommit(false);

                        // 🧾 Insertar el recibo
                        psRecibo.setInt(1, recibo.getIdEstudiante());
                        psRecibo.setDouble(2, recibo.getMontoTotal());
                        psRecibo.setDouble(3, recibo.getMontoPagado());
                        psRecibo.setBoolean(4, recibo.isEstado());
                        psRecibo.setDate(5, new java.sql.Date(recibo.getFechaPago().getTime()));
                        psRecibo.executeUpdate();

                        ResultSet rsRecibo = psRecibo.getGeneratedKeys();
                        if (!rsRecibo.next()) throw new SQLException("No se generó el ID del recibo.");
                        int idRecibo = rsRecibo.getInt(1);

                        // 🔗 Mapear IDs temporales
                        Map<Integer, Integer> mapaTemporal = new HashMap<>();

                        for (DetallesPago dp : nuevosDetalles) {
                                psDetalle.setInt(1, idRecibo);
                                psDetalle.setInt(2, dp.getIdTipoPago());
                                psDetalle.setString(3, dp.getMetodoPago());
                                psDetalle.setString(4, dp.getNumTrans());
                                psDetalle.setInt(5, dp.getIdAnoEscolar());
                                psDetalle.setString(6, dp.getDescripcion());
                                psDetalle.setString(7, dp.getMesCorrespondiente());
                                psDetalle.setDouble(8, dp.getMontoTotal());
                                psDetalle.setDouble(9, dp.getMontoPagado());
                                psDetalle.executeUpdate();

                                ResultSet rsDetalle = psDetalle.getGeneratedKeys();
                                if (!rsDetalle.next()) throw new SQLException("No se generó el ID del detalle.");
                                int idReal = rsDetalle.getInt(1);
                                mapaTemporal.put(dp.getId(), idReal);
                        }

                        // 💵 Insertar abonos y recolectar IDs únicos afectados
                        Set<Integer> idsAfectados = new HashSet<>();

                        for (Abono ab : abonos) {
                                int idDetalle = ab.getIdDetallesPago();
                                if (idDetalle < 0) {
                                        if (!mapaTemporal.containsKey(idDetalle)) throw new SQLException("ID temporal no encontrado.");
                                        idDetalle = mapaTemporal.get(idDetalle);
                                }

                                psAbono.setInt(1, idDetalle);
                                psAbono.setDate(2, Date.valueOf(ab.getFechaAbono()));
                                psAbono.setDouble(3, ab.getMontoAbonado());
                                psAbono.setString(4, ab.getDescripcion());
                                psAbono.setString(5, ab.getMetodoPago());
                                psAbono.setString(6, ab.getNumTrans());
                                psAbono.executeUpdate();

                                idsAfectados.add(idDetalle);
                        }

                        // 🔄 Sincronizar monto_pagado real con suma de abonos
                        for (int idDetalle : idsAfectados) {
                                psUpdateMonto.setInt(1, idDetalle);
                                psUpdateMonto.executeUpdate();
                        }

                        con.commit();
                        return true;

                } catch (SQLException e) {
                        System.out.println("Error en registrarPago: " + e.getMessage());
                        try { getConexion().rollback(); } catch (SQLException ex) {
                                System.out.println("Error en rollback: " + ex.getMessage());
                        }
                        return false;
                }
        }

}
