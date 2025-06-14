package expoferia.pagos.gestionpagos.dao;

import expoferia.pagos.gestionpagos.entidades.Representante;
import static expoferia.pagos.gestionpagos.conexion.Conexion.closeConnection;
import static expoferia.pagos.gestionpagos.conexion.Conexion.getConexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepresentanteDAO implements IRepresentanteDAO {

    @Override
    public ArrayList<Representante> lista(Integer id, String nombre) {
        ArrayList<Representante> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM representante WHERE 1=1");

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
                ps.setString(index, nombre);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Representante r = new Representante(
                        rs.getInt("id"),
                        rs.getString("cedula"),
                        rs.getString("nombre1"),
                        rs.getString("nombre2"),
                        rs.getString("apellido1"),
                        rs.getString("apellido2"),
                        rs.getString("telefono"),
                        rs.getString("correo"),
                        rs.getDate("fechaN"),
                        rs.getString("direccion"),
                        rs.getBoolean("estado")
                    );
                    lista.add(r);
                }
                closeConnection();
                return lista;
            }

        } catch (Exception e) {
            System.out.println("Error al listar representantes: " + e);
            return null;
        }
    }

    @Override
    public Integer buscarPorId(int id) {
        String sql = "SELECT id FROM representante WHERE id=?";
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar representante: " + e);
            closeConnection();
            return null;
        }
    }

    @Override
    public boolean agregar(Representante r) {
        String sql = "INSERT INTO representante(cedula, nombre1, nombre2, apellido1, apellido2, telefono, correo, fechaN, direccion, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getCedula());
            ps.setString(2, r.getNombre1());
            ps.setString(3, r.getNombre2());
            ps.setString(4, r.getApellido1());
            ps.setString(5, r.getApellido2());
            ps.setString(6, r.getTelefono());
            ps.setString(7, r.getCorreo());
            ps.setDate(8, r.getFechaN());
            ps.setString(9, r.getDireccion());
            ps.setBoolean(10, r.getEstado());

            int filas = ps.executeUpdate();
            closeConnection();
            return filas > 0;
        } catch (Exception e) {
            System.out.println("Error al agregar representante: " + e);
            closeConnection();
            return false;
        }
    }

    @Override
    public boolean modificar(Representante r) {
        StringBuilder sql = new StringBuilder("UPDATE representante SET ");
        List<Object> valores = new ArrayList<>();
        boolean hayComa = false;

        if (r.getCedula() != null) {
            sql.append("cedula=?");
            valores.add(r.getCedula());
            hayComa = true;
        }
        if (r.getNombre1() != null) {
            if (hayComa) sql.append(", ");
            sql.append("nombre1=?");
            valores.add(r.getNombre1());
            hayComa = true;
        }
        if (r.getNombre2() != null) {
            if (hayComa) sql.append(", ");
            sql.append("nombre2=?");
            valores.add(r.getNombre2());
            hayComa = true;
        }
        if (r.getApellido1() != null) {
            if (hayComa) sql.append(", ");
            sql.append("apellido1=?");
            valores.add(r.getApellido1());
            hayComa = true;
        }
        if (r.getApellido2() != null) {
            if (hayComa) sql.append(", ");
            sql.append("apellido2=?");
            valores.add(r.getApellido2());
            hayComa = true;
        }
        if (r.getTelefono() != null) {
            if (hayComa) sql.append(", ");
            sql.append("telefono=?");
            valores.add(r.getTelefono());
            hayComa = true;
        }
        if (r.getCorreo() != null) {
            if (hayComa) sql.append(", ");
            sql.append("correo=?");
            valores.add(r.getCorreo());
            hayComa = true;
        }
        if (r.getFechaN() != null) {
            if (hayComa) sql.append(", ");
            sql.append("fechaN=?");
            valores.add(r.getFechaN());
            hayComa = true;
        }
        if (r.getDireccion() != null) {
            if (hayComa) sql.append(", ");
            sql.append("direccion=?");
            valores.add(r.getDireccion());
            hayComa = true;
        }
        if (r.getEstado() != null) {
            if (hayComa) sql.append(", ");
            sql.append("estado=?");
            valores.add(r.getEstado());
        }

        if (valores.isEmpty()) {
            System.out.println("No hay cambios para actualizar.");
            return false;
        }

        sql.append(" WHERE id=?");
        valores.add(r.getId());

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < valores.size(); i++) {
                ps.setObject(i + 1, valores.get(i));
            }

            int filas = ps.executeUpdate();
            closeConnection();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("Error al modificar representante: " + e);
            closeConnection();
            return false;
        }
    }

    @Override
    public boolean desactivar(int id) {
        String sql = "UPDATE representante SET estado=false WHERE id=?";
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            closeConnection();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("Error al desactivar representante: " + e);
            closeConnection();
            return false;
        }
    }

    @Override
    public boolean activar(int id) {
        String sql = "UPDATE representante SET estado=true WHERE id=?";
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            closeConnection();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("Error al activar representante: " + e);
            closeConnection();
            return false;
        }
    }

}
