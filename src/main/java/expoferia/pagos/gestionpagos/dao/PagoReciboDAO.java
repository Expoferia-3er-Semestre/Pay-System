package expoferia.pagos.gestionpagos.dao;

import expoferia.pagos.gestionpagos.entidades.DetallesPago;
import expoferia.pagos.gestionpagos.entidades.PagoRecibo;
import static expoferia.pagos.gestionpagos.conexion.Conexion.*;

import java.sql.*;
import java.util.List;

public class PagoReciboDAO {

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

        public boolean registrarPago(PagoRecibo pago, List<DetallesPago> detalles) {
                String sqlRecibo = "INSERT INTO pago_recibo (id_estudiante, monto_total, monto_pagado, estado, fecha_pago) VALUES (?, ?, ?, ?, ?)";
                String sqlDetalle = "INSERT INTO detalles_pago (id_pago_recibo, id_tipo_pago, metodo_pago, num_trans, id_ano_escolar, descripcion, mes_correspondiente, monto_total, monto_pagado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                String sqlAbono = "INSERT INTO abono (id_detalles_pagos, fecha_abono, monto_abonado, descripcion, metodo_pago, num_trans) VALUES (?, ?, ?, ?, ?, ?)";

                try (Connection conn = getConexion();
                     PreparedStatement stmtRecibo = conn.prepareStatement(sqlRecibo, Statement.RETURN_GENERATED_KEYS);
                     PreparedStatement stmtDetalle = conn.prepareStatement(sqlDetalle, Statement.RETURN_GENERATED_KEYS);
                     PreparedStatement stmtAbono = conn.prepareStatement(sqlAbono)) {

                        conn.setAutoCommit(false); // 🔄 Iniciar transacción

                        // 1. Insertar pago_recibo
                        stmtRecibo.setInt(1, pago.getIdEstudiante());
                        stmtRecibo.setDouble(2, pago.getMontoTotal());
                        stmtRecibo.setDouble(3, pago.getMontoPagado());
                        stmtRecibo.setBoolean(4, pago.isEstado());
                        stmtRecibo.setDate(5, new java.sql.Date(pago.getFechaPago().getTime()));

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
                                                // 2. Insertar detalle de pago
                                                stmtDetalle.setInt(1, idRecibo);
                                                stmtDetalle.setInt(2, detalle.getIdTipoPago());
                                                stmtDetalle.setString(3, detalle.getMetodoPago());
                                                stmtDetalle.setString(4, detalle.getNumTrans());
                                                stmtDetalle.setInt(5, detalle.getIdAnoEscolar());
                                                stmtDetalle.setString(6, detalle.getDescripcion());
                                                stmtDetalle.setString(7, detalle.getMesCorrespondiente());
                                                stmtDetalle.setDouble(8, detalle.getMontoTotal());
                                                stmtDetalle.setDouble(9, detalle.getMontoPagado());

                                                stmtDetalle.executeUpdate();

                                                // 3. Obtener ID generado para el detalle
                                                try (ResultSet detalleKeys = stmtDetalle.getGeneratedKeys()) {
                                                        if (detalleKeys.next()) {
                                                                int idDetalleGenerado = detalleKeys.getInt(1);

                                                                // Si es un pago parcial, insertar abono
                                                                if (detalle.getMontoPagado() < detalle.getMontoTotal()) {
                                                                        stmtAbono.setInt(1, idDetalleGenerado);
                                                                        stmtAbono.setDate(2, new java.sql.Date(pago.getFechaPago().getTime()));
                                                                        stmtAbono.setDouble(3, detalle.getMontoPagado());
                                                                        stmtAbono.setString(4, detalle.getDescripcion());
                                                                        stmtAbono.setString(5, detalle.getMetodoPago());
                                                                        stmtAbono.setString(6, detalle.getNumTrans());

                                                                        stmtAbono.addBatch();
                                                                }
                                                        }
                                                }
                                        }

                                        stmtAbono.executeBatch();
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

}
