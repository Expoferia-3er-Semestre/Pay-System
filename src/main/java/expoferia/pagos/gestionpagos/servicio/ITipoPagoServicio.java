package expoferia.pagos.gestionpagos.servicio;

import expoferia.pagos.gestionpagos.entidades.TipoPago;

import java.util.ArrayList;

public interface ITipoPagoServicio extends IGenericInterface<TipoPago> {

    ArrayList<TipoPago> listar(String categoria, Boolean estado);

    boolean agregar(TipoPago tipoPago);

    boolean modificar(TipoPago tipoPago);

    boolean eliminar(int id, boolean estado);
}
