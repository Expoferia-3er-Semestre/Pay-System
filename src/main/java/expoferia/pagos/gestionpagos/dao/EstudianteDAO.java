package expoferia.pagos.gestionpagos.dao;

import expoferia.pagos.gestionpagos.entidades.Estudiante;
import static expoferia.pagos.gestionpagos.conexion.Conexion.closeConnection;
import static expoferia.pagos.gestionpagos.conexion.Conexion.getConexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {

    public ArrayList<Estudiante> lista(Integer id, String nombre) {
        ArrayList<Estudiante> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM estudiante WHERE 1=1");

        if (id != null && id > -1) {
            sql.append(" AND id = ?");
        }
        if (nombre != null && !nombre.isEmpty()) {
            sql.append(" AND nombre1 = ?");
        }
        sql.append(" ORDER BY nombre1 DESC, id");

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int index = 1;
            if (id != null && id > -1) {
                ps.setInt(index++, id);
            }
            if (nombre != null && !nombre.isEmpty()) {
                ps.setString(index++, nombre);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Estudiante estudiante = new Estudiante(
                            rs.getInt("id"),
                            rs.getString("cedulaRep"),
                            rs.getString("nombre1"),
                            rs.getString("nombre2"),
                            rs.getString("apellido1"),
                            rs.getString("apellido2"),
                            rs.getString("telefono"),
                            rs.getDate("fechaN"),
                            rs.getString("direccion"),
                            rs.getBoolean("estado")
                    );
                    lista.add(estudiante);
                }
                closeConnection();
                return lista;
            }

        } catch (Exception e) {
            System.out.println("Error al listar estudiantes: " + e);
            return null;
        }
    }

    public Integer buscarPorId(int id) {
        String sql = "SELECT id FROM estudiante WHERE id=?";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar estudiante: " + e);
            return null;
        }
    }

    public boolean agregar(Estudiante estudiante) {
        String sql = "INSERT INTO estudiante(cedulaRep, nombre1, nombre2, apellido1, apellido2, telefono, fechaN, direccion, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estudiante.getCedulaRep());
            ps.setString(2, estudiante.getNombre1());
            ps.setString(3, estudiante.getNombre2());
            ps.setString(4, estudiante.getApellido1());
            ps.setString(5, estudiante.getApellido2());
            ps.setString(6, estudiante.getTelefono());
            ps.setDate(7, estudiante.getFechaN());
            ps.setString(8, estudiante.getDireccion());
            ps.setBoolean(9, estudiante.getEstado());

            int filasAfectadas = ps.executeUpdate();
            closeConnection();
            return filasAfectadas > 0;
        } catch (Exception e) {
            System.out.println("Error al agregar estudiante: " + e);
            return false;
        }
    }

    public boolean modificar(Estudiante estudiante) {
        StringBuilder sql = new StringBuilder("UPDATE estudiante SET ");
        List<Object> valores = new ArrayList<>();
        boolean hayComa = false;

        if (estudiante.getCedulaRep() != null) {
            sql.append("cedulaRep=?");
            valores.add(estudiante.getCedulaRep());
            hayComa = true;
        }
        if (estudiante.getNombre1() != null) {
            if (hayComa) sql.append(", ");
            sql.append("nombre1=?");
            valores.add(estudiante.getNombre1());
            hayComa = true;
        }
        if (estudiante.getNombre2() != null) {
            if (hayComa) sql.append(", ");
            sql.append("nombre2=?");
            valores.add(estudiante.getNombre2());
            hayComa = true;
        }
        if (estudiante.getApellido1() != null) {
            if (hayComa) sql.append(", ");
            sql.append("apellido1=?");
            valores.add(estudiante.getApellido1());
            hayComa = true;
        }
        if (estudiante.getApellido2() != null) {
            if (hayComa) sql.append(", ");
            sql.append("apellido2=?");
            valores.add(estudiante.getApellido2());
            hayComa = true;
        }
        if (estudiante.getTelefono() != null) {
            if (hayComa) sql.append(", ");
            sql.append("telefono=?");
            valores.add(estudiante.getTelefono());
            hayComa = true;
        }
        if (estudiante.getFechaN() != null) {
            if (hayComa) sql.append(", ");
            sql.append("fechaN=?");
            valores.add(estudiante.getFechaN());
            hayComa = true;
        }
        if (estudiante.getDireccion() != null) {
            if (hayComa) sql.append(", ");
            sql.append("direccion=?");
            valores.add(estudiante.getDireccion());
            hayComa = true;
        }
        if (estudiante.getEstado() != null) {
            if (hayComa) sql.append(", ");
            sql.append("estado=?");
            valores.add(estudiante.getEstado());
        }

        if (valores.isEmpty()) {
            System.out.println("No hay cambios para actualizar.");
            return false;
        }

        sql.append(" WHERE id=?");
        valores.add(estudiante.getId());

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < valores.size(); i++) {
                ps.setObject(i + 1, valores.get(i));
            }

            int filasAfectadas = ps.executeUpdate();
            closeConnection();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al modificar estudiante: " + e);
            return false;
        }
    }

    public boolean desactivar(int id) {
        String sql = "UPDATE estudiante SET estado=false WHERE id=?";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            closeConnection();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al desactivar estudiante: " + e);
            return false;
        }
    }

    public boolean activar(int id) {
        String sql = "UPDATE estudiante SET estado=true WHERE id=?";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            closeConnection();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al activar estudiante: " + e);
            return false;
        }
    }
}
