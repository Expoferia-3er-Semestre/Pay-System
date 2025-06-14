package expoferia.pagos.gestionpagos.dao;

import expoferia.pagos.gestionpagos.entidades.Empleado;
import static expoferia.pagos.gestionpagos.conexion.Conexion.closeConnection;
import static expoferia.pagos.gestionpagos.conexion.Conexion.getConexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO implements IEmpleadoDAO{

    @Override
    public ArrayList<Empleado> lista(Integer id, String nombre) {
        ArrayList<Empleado> lista=new ArrayList<>();
        String sql="SELECT * FROM empleado";

        try (Connection con=getConexion();
             PreparedStatement ps=con.prepareStatement(sql)) {

            try (ResultSet rs=ps.executeQuery()){
                while (rs.next()) {
                    Empleado empleado=new Empleado(
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
                            rs.getBoolean("estado"),
                            rs.getString("contrasena"),
                            rs.getBoolean("rol")
                    );
                    lista.add(empleado);
                }
                closeConnection();
                return lista;
            }

        } catch (Exception e) {
            System.out.println("Error al listar empleados: "+e);
            closeConnection();
            return null;
        }
    }

    @Override
    public Integer buscarPorId(int id) {
        String sql="SELECT FROM empleado WHERE id=?";

        try (Connection con=getConexion();
        PreparedStatement ps=con.prepareStatement(sql)){
            ps.setInt(1, id);
            try (ResultSet rs=ps.executeQuery()){
                return rs.getInt("id");
            } catch (SQLException e) {
                System.out.println("Error al buscar empleado: "+e);
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar empleado: "+e);
            closeConnection();
            return null;
        }
    }

    @Override
    public boolean agregar(Empleado empleado) {
        String sql="INSERT INTO empleado(cedula, nombre1, " +
                "nombre2, apellido1, apellido2, " +
                "telefono, correo, fechaN, " +
                "direccion, estado, contrasena, rol) VALUES(" +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con=getConexion();
        PreparedStatement ps=con.prepareStatement(sql)){

            ps.setString(1, empleado.getCedula());
            ps.setString(2, empleado.getNombre1());
            ps.setString(3, empleado.getNombre2());
            ps.setString(4, empleado.getApellido1());
            ps.setString(5, empleado.getApellido2());
            ps.setString(6, empleado.getTelefono());
            ps.setString(7, empleado.getCorreo());
            ps.setDate(8, empleado.getFechaN());
            ps.setString(9, empleado.getDireccion());
            ps.setBoolean(10, empleado.getEstado());
            ps.setString(11, empleado.getContrasena());
            ps.setBoolean(12, empleado.getRol());

            int filasAfectadas=ps.executeUpdate();
            closeConnection();
            return filasAfectadas>0;
        } catch (Exception e) {
            System.out.println("Error al agregar un empleado: "+e);
            closeConnection();
            return false;
        }
    }

    @Override
    public boolean modificar(Empleado empleado) {
        StringBuilder sql = new StringBuilder("UPDATE empleado SET ");
        List<Object> valores = new ArrayList<>();
        boolean hayComa = false;

        // Construcción dinámica de la consulta
        if (empleado.getCedula() != null) {
            sql.append("cedula=?");
            valores.add(empleado.getCedula());
            hayComa = true;
        }
        if (empleado.getNombre1() != null) {
            if (hayComa) sql.append(", ");
            sql.append("nombre1=?");
            valores.add(empleado.getNombre1());
            hayComa = true;
        }
        if (empleado.getNombre2() != null) {
            if (hayComa) sql.append(", ");
            sql.append("nombre2=?");
            valores.add(empleado.getNombre2());
            hayComa = true;
        }
        if (empleado.getApellido1() != null) {
            if (hayComa) sql.append(", ");
            sql.append("apellido1=?");
            valores.add(empleado.getApellido1());
            hayComa = true;
        }
        if (empleado.getApellido2() != null) {
            if (hayComa) sql.append(", ");
            sql.append("apellido2=?");
            valores.add(empleado.getApellido2());
            hayComa = true;
        }
        if (empleado.getTelefono() != null) {
            if (hayComa) sql.append(", ");
            sql.append("telefono=?");
            valores.add(empleado.getTelefono());
            hayComa = true;
        }
        if (empleado.getCorreo() != null) {
            if (hayComa) sql.append(", ");
            sql.append("correo=?");
            valores.add(empleado.getCorreo());
            hayComa = true;
        }
        if (empleado.getFechaN() != null) {
            if (hayComa) sql.append(", ");
            sql.append("fechaN=?");
            valores.add(empleado.getFechaN());
            hayComa = true;
        }
        if (empleado.getDireccion() != null) {
            if (hayComa) sql.append(", ");
            sql.append("direccion=?");
            valores.add(empleado.getDireccion());
            hayComa = true;
        }
        if (empleado.getEstado() != null) {
            if (hayComa) sql.append(", ");
            sql.append("estado=?");
            valores.add(empleado.getEstado());
            hayComa = true;
        }
        if (empleado.getContrasena() != null) {
            if (hayComa) sql.append(", ");
            sql.append("contrasena=?");
            valores.add(empleado.getContrasena());
            hayComa = true;
        }
        if (empleado.getRol() != null) {
            if (hayComa) sql.append(", ");
            sql.append("rol=?");
            valores.add(empleado.getRol());
        }

        // Si no hay valores para actualizar, cancelamos la ejecución
        if (valores.isEmpty()) {
            System.out.println("No hay cambios para actualizar.");
            return false;
        }

        // Agregar condición WHERE para asegurar que solo se modifique el empleado correcto
        sql.append(" WHERE id=?");
        valores.add(empleado.getId());

        // Ejecutar la consulta con PreparedStatement
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < valores.size(); i++) {
                ps.setObject(i + 1, valores.get(i));
            }

            int filasAfectadas = ps.executeUpdate();
            closeConnection();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al modificar Empleado: " + e);
            closeConnection();
            return false;
        }
    }

    @Override
    public boolean desactivar(int id) {
        String sql="UPDATE empleado SET estado=false WHERE id=?";

        try (Connection con=getConexion();
        PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filasAfectadas= ps.executeUpdate();
            closeConnection();
            return filasAfectadas>0;
        } catch (SQLException e) {
            System.out.println("Error al desactivar empleado: "+e);
            closeConnection();
            return false;
        }

    }

    @Override
    public boolean activar(int id) {
        String sql="UPDATE empleado SET estado=true WHERE id=?";

        try (Connection con=getConexion();
             PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filasAfectadas= ps.executeUpdate();
            closeConnection();
            return filasAfectadas>0;
        } catch (SQLException e) {
            System.out.println("Error al activar empleado: "+e);
            closeConnection();
            return false;
        }

    }

    @Override
    public Empleado buscarContrasena(String correo) {
        
        if (correo!=null) {

            String sql="SELECT * FROM empleado WHERE correo=?";

            try (Connection con=getConexion();
            PreparedStatement ps=con.prepareStatement(sql)) {
                ps.setString(1, correo);
                try (ResultSet rs=ps.executeQuery()){
                    if (rs.next()) {  // Verifica si hay resultados antes de extraer los datos del empleado

                        Empleado empleado=new Empleado(
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
                                rs.getBoolean("estado"),
                                rs.getString("contrasena"),
                                rs.getBoolean("rol"));
                        closeConnection();
                        return empleado;
                    }
                } catch (Exception e) {
                    System.out.println("Error al encontrar el empleado: "+e.getMessage());
                    closeConnection();
                }
            }   catch (Exception e) {
                System.out.println("Error al obtener datos del empleado: " + e.getMessage());
                closeConnection();
                }
        }
        return null;
    }

}
