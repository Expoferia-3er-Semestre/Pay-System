package expoferia.pagos.gestionpagos.dao;

import expoferia.pagos.gestionpagos.entidades.Representante;
import java.util.ArrayList;

public interface IRepresentanteDAO extends IGenericInterface<Representante> {
    ArrayList<Representante> lista(Integer id, String nombre);
    Integer buscarPorId(int id);
    boolean agregar(Representante representante);
    boolean modificar(Representante representante);
    boolean desactivar(int id);
    boolean activar(int id);
}
