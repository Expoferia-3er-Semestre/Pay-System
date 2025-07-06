package expoferia.pagos.gestionpagos.gui.modulos;

import expoferia.pagos.gestionpagos.entidades.DetallesPago;
import expoferia.pagos.gestionpagos.dao.DetallesPagoDAO;

import java.util.*;
import java.util.stream.Collectors;

public class GestorPagosEstudiante {

    private List<DetallesPago> listaPagos;
    private final DetallesPagoDAO detallesPagoDAO = new DetallesPagoDAO();

    private static final List<String> MESES_ESCOLARES = Arrays.asList(
            "Septiembre", "Octubre", "Noviembre", "Diciembre",
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto"
    );

    public GestorPagosEstudiante(int idEstudiante, int idAnoEscolar) {
        listaPagos = detallesPagoDAO.listarPorEstudiante(idEstudiante, idAnoEscolar);
        System.out.println("Pagos: " + getTodosLosPagos().size());

    }

    public List<DetallesPago> filtrarPorTipo(String tipo) {
        return listaPagos.stream()
                .filter(p -> tipo.equalsIgnoreCase(p.getCategoria()))
                .collect(Collectors.toList());
    }

    public List<DetallesPago> ordenarMensualidades() {
        return listaPagos.stream()
                .filter(p -> "Mensualidad".equalsIgnoreCase(p.getCategoria()))
                .sorted(Comparator.comparingInt(p ->
                        MESES_ESCOLARES.indexOf(p.getMesCorrespondiente()))
                ).collect(Collectors.toList());
    }

    public double getTotalPagado() {
        return listaPagos.stream()
                .mapToDouble(DetallesPago::getMontoPagado)
                .sum();
    }

    public double getTotalPendiente() {
        return listaPagos.stream()
                .filter(DetallesPago::tieneSaldoPendiente)
                .mapToDouble(DetallesPago::getDiferencia)
                .sum();
    }

    public List<DetallesPago> getTodosLosPagos() {
        return listaPagos;
    }
}
