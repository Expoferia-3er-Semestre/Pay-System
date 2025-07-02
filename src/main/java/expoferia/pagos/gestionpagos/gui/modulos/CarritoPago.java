package expoferia.pagos.gestionpagos.gui.modulos;

import expoferia.pagos.gestionpagos.entidades.Abono;
import expoferia.pagos.gestionpagos.entidades.DetallesPago;
import expoferia.pagos.gestionpagos.gui.tabla.DetallePagoTableModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CarritoPago {

    private DetallePagoTableModel tablaModelo;
    private final List<Abono> abonos = new ArrayList<>();
    private final List<DetallesPago> conceptos = new ArrayList<>();

    public void agregarConcepto(DetallesPago concepto) {
        if (!yaExiste(concepto)) {
            conceptos.add(concepto);
            if (tablaModelo != null) {
                tablaModelo.agregarFila(concepto, concepto.getMontoPagado());
            }
        }
    }

    public void setTablaModelo(DetallePagoTableModel modelo) {
        this.tablaModelo = modelo;
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

    public double calcularTotalPagado() {
        return conceptos.stream()
                .mapToDouble(DetallesPago::getMontoPagado)
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
                .filter(DetallesPago::esMensualidad)
                .count();
    }

    public List<DetallesPago> getDetallesConDiferencia() {
        return conceptos.stream()
                .filter(dp ->
                        dp.esMensualidad() &&
                                dp.tieneSaldoPendiente()
                )
                .collect(Collectors.toList());
    }

    public void agregarAbono(Abono abono) {
        abonos.add(abono);
        if (tablaModelo != null) {
            tablaModelo.agregarFilaAbono(abono, abono.getMontoAbonado());
        }
    }

    public List<Abono> getAbonos() {
        return abonos;
    }

    public boolean estaVacioAbono() {
        return abonos.isEmpty();
    }

    public void limpiarAbonos() {
        abonos.clear();
    }

    public void limpiarTodo() {
        abonos.clear();
        conceptos.clear();
        if (tablaModelo != null) tablaModelo.limpiarTabla();
    }

}
