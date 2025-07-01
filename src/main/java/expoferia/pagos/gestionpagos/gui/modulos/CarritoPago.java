package expoferia.pagos.gestionpagos.gui.modulos;

import expoferia.pagos.gestionpagos.entidades.DetallesPago;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CarritoPago {
    private final List<DetallesPago> conceptos = new ArrayList<>();

    public void agregarConcepto(DetallesPago concepto) {
        if (!yaExiste(concepto)) {
            conceptos.add(concepto);
        }
    }

    public void eliminarConcepto(int index) {
        if (index >= 0 && index < conceptos.size()) {
            conceptos.remove(index);
        }
    }

    public List<DetallesPago> getConceptos() {
        return conceptos;
    }

    public void limpiar() {
        conceptos.clear();
    }

    public double calcularTotal() {
        return conceptos.stream()
                .mapToDouble(DetallesPago::getMontoTotal)
                .sum();
    }

    public boolean yaExiste(DetallesPago nuevo) {
        return conceptos.stream().anyMatch(c ->
                Objects.equals(c.getIdTipoPago(), nuevo.getIdTipoPago()) &&
                        Objects.equals(c.getMesCorrespondiente(), nuevo.getMesCorrespondiente())
        );
    }

    public boolean estaVacio() {
        return conceptos.isEmpty();
    }

    public long contarMensualidades() {
        return conceptos.stream()
                .filter(c -> "Mensualidad".equalsIgnoreCase(c.getCategoria()))
                .count();
    }

//    public double calcularTotalPagado() {
//        return conceptos.stream()
//                .flatMap(c -> c.getMetodosPago().stream())
//                .mapToDouble(MetodoPago::getMonto)
//                .sum();
//    }
//
//    public boolean requiereBloqueo(DetallesPago concepto) {
//        return concepto.tieneSaldoPendiente() && !concepto.isEsAbonoPermitido();
//    }

//    public boolean tieneErroresDePago() {
//        return conceptos.stream().anyMatch(this::requiereBloqueo);
//    }
}
