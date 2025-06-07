package expoferia.pagos.gestionpagos.dao;

public interface IGenericInterface<T> {

    boolean agregar(T objeto);

    Integer buscarPorId(int id);

    boolean modificar(T objeto);

    boolean desactivar(int id);

    boolean activar(int id);
}
