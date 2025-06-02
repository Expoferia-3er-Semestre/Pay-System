package expoferia.pagos.gestionpagos.servicio;

public interface IGenericInterface<T> {

    boolean agregar(T objeto);

    boolean modificar(T objeto);

    boolean eliminar(int id, boolean estado);

}
