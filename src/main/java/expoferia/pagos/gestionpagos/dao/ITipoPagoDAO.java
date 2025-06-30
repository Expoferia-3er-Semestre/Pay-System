package expoferia.pagos.gestionpagos.dao;

import expoferia.pagos.gestionpagos.entidades.TipoPago;

import java.util.ArrayList;

public interface ITipoPagoDAO extends IGenericInterface<TipoPago> {

    ArrayList<TipoPago> listar(String categoria, Boolean estado);

    TipoPago buscarPorId(String categoria);

    boolean agregar(TipoPago tipoPago);

    boolean modificar(TipoPago tipoPago);

    boolean desactivar(int id);

    boolean activar(int id);
}
