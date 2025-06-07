package expoferia.pagos.gestionpagos.dao;

import static expoferia.pagos.gestionpagos.conexion.Conexion.closeConnection;
import static expoferia.pagos.gestionpagos.conexion.Conexion.getConexion;
import expoferia.pagos.gestionpagos.entidades.TipoPago;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TipoPagoDAO implements ITipoPagoDAO {

    @Override
    public ArrayList<TipoPago> listar(String categoria, Boolean estado) {

        ArrayList<TipoPago> lista=new ArrayList<>();
        String sql="SELECT * FROM tipo_pago WHERE 1=1";

        if (categoria!=null && !categoria.isEmpty()) { sql+=" AND categoria = ?";}
        if (estado!=null) {sql+=" AND estado = ?";}
        sql+=" ORDER BY estado DESC, id";

        try (Connection con=getConexion();
             PreparedStatement ps=con.prepareStatement(sql))
             {

            if (categoria!=null && !categoria.isEmpty()) {
                ps.setString(1, categoria);
            }
            if (estado!=null) {
                ps.setBoolean(2, estado);
            }

            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()) {

                    TipoPago nuevoTPago=new TipoPago(
                            rs.getInt("id"),
                            rs.getString("concepto"),
                            rs.getString("categoria"),
                            rs.getDouble("costo"),
                            rs.getBoolean("estado"));

                    lista.add(nuevoTPago);
                }
                closeConnection();
                return lista;
            }

        } catch (Exception e) {
            System.out.println("Error al listar TipoPagos: "+e);
            return null;
        }
    }

    @Override
    public Integer buscarPorId(int id) {
        String sql="SELECT FROM tipopago WHERE id=?";

        try (Connection con=getConexion();
        PreparedStatement ps=con.prepareStatement(sql)){
            ps.setInt(1, id);

            try (ResultSet rs=ps.executeQuery()){
                System.out.println("Si existe el tipo de pago.");
                return rs.getInt("id");
            } catch (SQLException e) {
                System.out.println("Error al encontrar tipo de pago: "+e);
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error al encontrar tipo de pago: "+e);
            return null;
        }
    }

    @Override
    public boolean agregar(TipoPago tipoPago) {
        String sql="INSERT INTO tipo_pago(concepto, categoria, costo) " +
        " VALUES(?, ?, ?)";
        try (Connection con=getConexion();
        PreparedStatement ps=con.prepareStatement(sql)){

                ps.setString(1, tipoPago.getConcepto());
                ps.setString(2, tipoPago.getCategoria());
                ps.setDouble(3, tipoPago.getCosto());

                int filasAfectadas=ps.executeUpdate();
                closeConnection();
                return filasAfectadas>0;
        } catch (Exception e) {
            System.out.println("Error al agregar TipoPago: "+e);
            return false;
        }

    }

    @Override
    public boolean modificar(TipoPago tipoPago) {
        StringBuilder sql=new StringBuilder("UPDATE tipo_pago SET ");
        boolean hayComa=false;
        List<Object> valores=new ArrayList<>();

        if (tipoPago.getConcepto()!=null) {
            sql.append("concepto=?");
            hayComa=true;
            valores.add(tipoPago.getConcepto());
        }

        if (tipoPago.getCategoria()!=null) {
            if (hayComa) sql.append(", ");
            sql.append("categoria=?");
            valores.add(tipoPago.getCategoria());
            hayComa=true;
        }

        if (tipoPago.getCosto()!=null) {
            if (hayComa) sql.append(", ");
            sql.append("costo=?");
            valores.add(tipoPago.getCosto());
        }

        sql.append(" WHERE id=?");

        if (valores.isEmpty()) {
            System.out.println("No hay campos para actualizar.");
            return false;
        }
        valores.add(tipoPago.getId());

        try (Connection con=getConexion();
             PreparedStatement ps= con.prepareStatement(sql.toString())) {

            for (int i=0;i<valores.size();i++) {
                ps.setObject(i+1, valores.get(i));
            }

            int filasAfectadas=ps.executeUpdate();
            closeConnection();
            return filasAfectadas>0;
        } catch (Exception e) {
            System.out.println("Error al modificar el tipo de pago: "+e);
            return false;
        }
    }

    @Override
    public boolean desactivar(int id) {
        String sql="UPDATE tipo_pago SET estado=false WHERE id = ?";

        try (Connection con=getConexion();
        PreparedStatement ps=con.prepareStatement(sql)){

            ps.setInt(1, id);
            int filasAfectadas= ps.executeUpdate();
            closeConnection();
            return filasAfectadas>0;

        } catch (Exception e) {
            System.out.println("Error al desactivar TipoPago: "+e);
            return false;
        }
    }

    @Override
    public boolean activar(int id) {
        String sql="UPDATE tipo_pago SET estado=true WHERE id = ?";

        try (Connection con=getConexion();
             PreparedStatement ps=con.prepareStatement(sql)){

            ps.setInt(1, id);
            int filasAfectadas= ps.executeUpdate();
            closeConnection();
            return filasAfectadas>0;

        } catch (Exception e) {
            System.out.println("Error al activar TipoPago: "+e);
            return false;
        }
    }

}

