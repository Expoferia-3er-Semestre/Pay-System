package expoferia.pagos.gestionpagos.dao;

import expoferia.pagos.gestionpagos.entidades.Empleado;

import java.util.ArrayList;

public interface IEmpleadoDAO extends IGenericInterface<Empleado>{

    ArrayList<Empleado> lista(Integer id, String nombre);

    Integer buscarPorId(int id);

    boolean agregar(Empleado empleado);

    boolean modificar(Empleado empleado);

    boolean desactivar(int id);

    boolean activar(int id);

    String buscarContrasena(String correo);
    }
