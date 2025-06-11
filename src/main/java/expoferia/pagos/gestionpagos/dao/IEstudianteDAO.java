package expoferia.pagos.gestionpagos.dao;

import expoferia.pagos.gestionpagos.entidades.Estudiante;

import java.util.ArrayList;

public interface IEstudianteDAO extends IGenericInterface<Estudiante> {

    ArrayList<Estudiante> lista(Integer id, String nombre);

    Integer buscarPorId(int id);

    boolean agregar(Estudiante estudiante);

    boolean modificar(Estudiante estudiante);

    boolean desactivar(int id);

    boolean activar(int id);
}