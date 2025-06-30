package expoferia.pagos.gestionpagos.dao;

import expoferia.pagos.gestionpagos.entidades.DetallesPago;
import expoferia.pagos.gestionpagos.entidades.PagoRecibo;
import static expoferia.pagos.gestionpagos.conexion.Conexion.*;

import java.sql.*;
import java.util.List;

public class PagoReciboDAO {

        public boolean registrarPago(PagoRecibo pago, List<DetallesPago> detalles) {
                String sqlRecibo = "INSERT INTO pago_recibo (id_estudiante, monto_total, monto_pagado, metodo_pago, estado, fecha_pago) VALUES (?, ?, ?, ?, ?, ?)";
                String sqlDetalle = "INSERT INTO detalles_pago (id_pago_recibo, id_tipo_pago, num_trans, id_ano_escolar, descripcion, mes_correspondiente) VALUES (?, ?, ?, ?, ?, ?)";

                try (Connection conn = getConexion();
                     PreparedStatement stmtRecibo = conn.prepareStatement(sqlRecibo, Statement.RETURN_GENERATED_KEYS);
                     PreparedStatement stmtDetalle = conn.prepareStatement(sqlDetalle)) {

                        conn.setAutoCommit(false); // Transacción iniciada

                        // 1. Insertar pago_recibo
                        stmtRecibo.setInt(1, pago.getIdEstudiante());
                        stmtRecibo.setDouble(2, pago.getMontoTotal());
                        stmtRecibo.setDouble(3, pago.getMontoPagado());
                        stmtRecibo.setString(4, pago.getMetodoPago());
                        stmtRecibo.setBoolean(5, pago.isEstado());
                        stmtRecibo.setDate(6, new java.sql.Date(pago.getFechaPago().getTime()));

                        int affectedRows = stmtRecibo.executeUpdate();
                        if (affectedRows == 0) {
                                conn.rollback();
                                closeConnection();
                                return false;
                        }

                        try (ResultSet generatedKeys = stmtRecibo.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                        int idRecibo = generatedKeys.getInt(1);

                                        for (DetallesPago detalle : detalles) {
                                                stmtDetalle.setInt(1, idRecibo);
                                                stmtDetalle.setInt(2, detalle.getIdTipoPago());
                                                stmtDetalle.setString(3, detalle.getNumTrans());
                                                stmtDetalle.setInt(4, detalle.getIdAnoEscolar());
                                                stmtDetalle.setString(5, detalle.getDescripcion());
                                                stmtDetalle.setString(6, detalle.getMesCorrespondiente());
                                                stmtDetalle.addBatch();
                                        }

                                        stmtDetalle.executeBatch();
                                        conn.commit();
                                        closeConnection();
                                        return true;
                                } else {
                                        conn.rollback();
                                        closeConnection();
                                        return false;
                                }
                        }

                } catch (Exception ex) {
                        ex.printStackTrace();
                        closeConnection();
                        return false;
                }
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

}
