package expoferia.pagos.gestionpagos.dao;


import expoferia.pagos.gestionpagos.entidades.AnioEscolar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static expoferia.pagos.gestionpagos.conexion.Conexion.closeConnection;
import static expoferia.pagos.gestionpagos.conexion.Conexion.getConexion;

public class AnoEscolarDAO {

    public Integer obtenerAnoEscolarActivo() {
        String sql = "SELECT id_ano_escolar FROM ano_escolar WHERE estado = 1 AND ? BETWEEN periodo_inicio AND periodo_fin";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(System.currentTimeMillis()));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    AnioEscolar aEs = new AnioEscolar(
                            rs.getInt("id_ano_escolar"),
                            rs.getDate(""),
                            rs.getDate(""),
                            rs.getBoolean("")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar año escolar activo: " + e.getMessage());
        } finally {
            closeConnection();
        }

        return null;
    }

    public AnioEscolar obtenerPeriodoActivo() {
        String sql = "SELECT id_ano_escolar, periodo_inicio, periodo_fin, estado " +
                "FROM ano_escolar " +
                "WHERE estado = 1 AND ? BETWEEN periodo_inicio AND periodo_fin";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(System.currentTimeMillis()));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AnioEscolar(
                            rs.getInt("id_ano_escolar"),
                            rs.getDate("periodo_inicio"),
                            rs.getDate("periodo_fin"),
                            rs.getBoolean("estado")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener año escolar activo: " + e.getMessage());
        } finally {
            closeConnection();
        }

        return null;
    }

    public List<AnioEscolar> listarPeriodosEscolares() {
        List<AnioEscolar> lista = new ArrayList<>();

        String sql = "SELECT id_ano_escolar, periodo_inicio, periodo_fin, estado FROM ano_escolar ORDER BY periodo_inicio DESC";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                AnioEscolar anio = new AnioEscolar(
                        rs.getInt("id_ano_escolar"),
                        rs.getDate("periodo_inicio"),
                        rs.getDate("periodo_fin"),
                        rs.getBoolean("estado")
                );
                lista.add(anio);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar años escolares: " + e.getMessage());
        } finally {
            closeConnection();
        }

        return lista;
    }


}

