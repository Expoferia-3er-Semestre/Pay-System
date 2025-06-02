package expoferia.pagos.gestionpagos.servicio;

import expoferia.pagos.gestionpagos.conexion.Conexion;
import expoferia.pagos.gestionpagos.entidades.TipoPago;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class TipoPagoServicio implements ITipoPagoServicio{

    Conexion conexion=new Conexion();

    @Override
    public ArrayList<TipoPago> listar(String categoria, Boolean estado) {

        ArrayList<TipoPago> lista=new ArrayList<>();

        String sql="SELECT * FROM tipo_pago WHERE 1=1";

        if (categoria!=null && !categoria.isEmpty()) { sql+=" AND categoria = ?";}

        if (estado!=null) {sql+=" AND estado = ?";}

        sql+=" ORDER BY estado DESC, id";

        try (Connection con=conexion.getConexion();
             PreparedStatement ps= con.prepareStatement(sql))
             {

            if (categoria!=null && !categoria.isEmpty()) {
                ps.setString(1, categoria);
            }

            if (estado!=null) {
                ps.setBoolean(2, estado);
            }
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                TipoPago nuevoTPago=new TipoPago(
                        rs.getInt("id"),
                        rs.getString("concepto"),
                        rs.getString("categoria"),
                        rs.getDouble("costo"),
                        rs.getBoolean("estado"));

                lista.add(nuevoTPago);
            }
            rs.close();
            conexion.closeConnection();
            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar TipoPagos: "+e);
            return null;
        }
    }

    @Override
    public boolean agregar(TipoPago tipoPago) {
        String sql="INSERT INTO tipo_pago(concepto, categoria, costo) " +
        " VALUES(?, ?, ?)";
        try (Connection con=conexion.getConexion();
        PreparedStatement ps=con.prepareStatement(sql)){

                ps.setString(1, tipoPago.getConcepto());
                ps.setString(2, tipoPago.getCategoria());
                ps.setDouble(3, tipoPago.getCosto());
                int filasAfectadas=ps.executeUpdate();

                return filasAfectadas>0;
        } catch (Exception e) {
            System.out.println("Error al agregar TipoPago: "+e);
            return false;
        }

    }

    @Override
    public boolean modificar(TipoPago tipoPago) {
        var sql="UPDATE tipo_pago SET concepto=?, categoria=?, costo=?"+
                " WHERE id=?";

        try (Connection con=conexion.getConexion();
             PreparedStatement ps= con.prepareStatement(sql)) {

            ps.setString(1, tipoPago.getConcepto());
            ps.setString(2, tipoPago.getCategoria());
            ps.setDouble(3, tipoPago.getCosto());
            ps.setInt(4, tipoPago.getId());
            int filasAfectadas=ps.executeUpdate();
            return filasAfectadas>0;
        } catch (Exception e) {
            System.out.println("Error al modificar el cliente: "+e);
            return false;
        }
    }

    @Override
    public boolean eliminar(int id, boolean estado) {
        String sql="UPDATE tipo_pago SET estado = ? WHERE id = ?";

        try (Connection con= conexion.getConexion();
        PreparedStatement ps=con.prepareStatement(sql)){

            ps.setBoolean(1, estado);
            ps.setInt(2, id);
            int filasAfectadas= ps.executeUpdate();
            return filasAfectadas>0;

        } catch (Exception e) {
            System.out.println("Error al desactivar TipoPago: "+e);
            return false;
        }
    }

    public static void main(String[] args) {

        TipoPagoServicio tps=new TipoPagoServicio();
        ArrayList<TipoPago> list=tps.listar("Mensualidad", null);

        for (TipoPago tipoPago:list) {
            System.out.println(tipoPago);
        }

        boolean exito=tps.agregar(list.getFirst());
        if (exito) {
            System.out.println("Exito");
        } else {
            System.out.println("Fracaso");
        }
    }

}

