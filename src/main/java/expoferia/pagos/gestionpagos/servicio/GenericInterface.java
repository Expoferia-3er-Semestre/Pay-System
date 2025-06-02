package expoferia.pagos.gestionpagos.servicio;

import java.util.ArrayList;
import java.util.List;

public interface GenericInterface<T> {

    ArrayList<T> listar();

    ArrayList<T> filtrar();

    boolean agregar(T objeto);

    boolean modificar(T objeto);

    boolean eliminar(int id, boolean estado);

}
