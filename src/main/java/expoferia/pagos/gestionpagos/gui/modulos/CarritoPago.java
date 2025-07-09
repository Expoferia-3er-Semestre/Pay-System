package expoferia.pagos.gestionpagos.gui.modulos;

import expoferia.pagos.gestionpagos.entidades.Abono;
import expoferia.pagos.gestionpagos.entidades.DetallesPago;
import expoferia.pagos.gestionpagos.gui.tabla.DetallePagoTableModel;
import expoferia.pagos.gestionpagos.util.ConfigGeneral;

import java.util.*;
import java.util.stream.Collectors;

public class CarritoPago {

    private DetallePagoTableModel tablaModelo;
    private final List<Abono> abonos = new ArrayList<>();
    private final List<DetallesPago> conceptos = new ArrayList<>();
    private int contadorIdTemporal = 1;
    public int generarIdTemporal() {
        return contadorIdTemporal++;
    }

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

    public static Map<String, Double> obtenerMontosPorMetodoPago(List<DetallesPago> listaPagos) {
        Map<String, Double> totales = new HashMap<>();

        for (DetallesPago pago : listaPagos) {
            // Método principal del pago
            String metodoPrincipal = pago.getMetodoPago();
            totales.merge(metodoPrincipal, pago.getMontoPagado(), Double::sum);

            // Abonos asociados
            List<Abono> abonos = pago.getAbonos();
            if (abonos != null) {
                for (Abono ab : abonos) {
                    String metodoAbono = ab.getMetodoPago();
                    totales.merge(metodoAbono, ab.getMontoAbonado(), Double::sum);
                }
            }
        }

        return totales;
    }


    public List<DetallesPago> getConceptos() {
        return conceptos;
    }

    public boolean tieneMensualidadesAsignadas() {
        return conceptos.stream()
                .anyMatch(dp -> dp.getMesCorrespondiente() != null && !dp.getMesCorrespondiente().isBlank());
    }

    public boolean hayConceptosSinAbono() {
        Set<Integer> idsConAbono = abonos.stream()
                .map(Abono::getIdDetallesPago)
                .collect(Collectors.toSet());

        return conceptos.stream()
                .anyMatch(dp -> !idsConAbono.contains(dp.getId()));
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
        return conceptos.stream().anyMatch(c -> {
            boolean mismoId = Objects.equals(c.getIdTipoPago(), nuevo.getIdTipoPago());

            boolean ambosConMes = c.getMesCorrespondiente() != null && nuevo.getMesCorrespondiente() != null;
            boolean mismoMes = ambosConMes && c.getMesCorrespondiente().equalsIgnoreCase(nuevo.getMesCorrespondiente());

            // Solo si ambos tienen mes válido, se compara el mes
            // Si no tienen mes, no se consideran duplicados por eso
            return mismoId && (mismoMes || !ambosConMes);
        });
    }


    public boolean estaVacioConceptos() {
        return conceptos.isEmpty();
    }

    public long contarMensualidades() {
        return conceptos.stream()
                .filter(DetallesPago::esMensualidad)
                .count();
    }

    public void agregarAbonoAConcepto(DetallesPago concepto, Abono abono) {
        if (!yaExiste(concepto)) {
            conceptos.add(concepto);
        }

            abonos.add(abono);
            abonarAConcepto(abono.getIdDetallesPago(), abono.getMontoAbonado());

            // Actualizar tabla si corresponde
            if (tablaModelo != null) {
                tablaModelo.agregarFilaAbono(abono, abono.getMontoAbonado());
            }

    }

    public void agregarConceptoConAbono(DetallesPago concepto, Abono abono) {
        if (!yaExiste(concepto)) {
            conceptos.add(concepto);

            // Enlazar el abono al concepto
            abono.setIdDetallesPago(concepto.getId());

            abonos.add(abono);

            // Actualizar tabla si corresponde
            if (tablaModelo != null) {
                tablaModelo.agregarFilaAbono(abono, abono.getMontoAbonado());
            }
        }
    }

    public List<DetallesPago> getDetallesConDiferencia() {
        return conceptos.stream()
                .filter(dp ->
                        dp.esMensualidad() &&
                                dp.tieneSaldoPendiente()
                )
                .collect(Collectors.toList());
    }

    public List<Abono> getAbonos() {
        return abonos;
    }

    public Abono getAbonoUnico() { return abonos.getFirst(); }

    public boolean estaVacioAbonos() {
        return abonos.isEmpty();
    }

    public void abonarAConcepto(int idDetallePago, double monto) {
        for (DetallesPago dp : conceptos) {
            if (dp.getId() == idDetallePago) {
                dp.setMontoPagado(dp.getMontoPagado() + monto);
                break;
            }
        }
    }

    public void agregarAbono(Abono abono) {
        abonos.add(abono);
        abonarAConcepto(abono.getIdDetallesPago(), abono.getMontoAbonado());
        if (tablaModelo != null) {
            tablaModelo.agregarFilaAbono(abono, abono.getMontoAbonado());
        }
    }

    public DetallesPago buscarMensualidadPendiente(List<DetallesPago> pagadosBD) {
        for (DetallesPago dp : conceptos) {
            if (!dp.esMensualidad()) continue;

            String mesCarrito = dp.getMesCorrespondiente();

            // 🔍 Verificar si la misma mensualidad está pagada en el carrito
            if (dp.getMontoPagado() >= dp.getMontoTotal()) {
                continue; // ✅ Ya cubierta en el carrito, no importa si vino de BD
            }

            // 📚 Buscar en BD si ese mes ya fue cubierto allá también
            boolean yaEstaPagadoEnBD = pagadosBD.stream()
                    .filter(p -> p.esMensualidad())
                    .filter(p -> mesCarrito.equals(p.getMesCorrespondiente()))
                    .anyMatch(p -> p.getMontoPagado() >= p.getMontoTotal());

            if (yaEstaPagadoEnBD) continue;

            return dp; // Este mes aún tiene deuda pendiente
        }

        return null;
    }

    public void limpiarAbonos() {
        abonos.clear();
    }

    public void aplicarAbonosATodos() {

        Map<Integer, Double> totalAbonosPorDetalle = abonos.stream()
                .collect(Collectors.groupingBy(
                        Abono::getIdDetallesPago,
                        Collectors.summingDouble(Abono::getMontoAbonado)
                ));

        for (DetallesPago dp : conceptos) {
            double adicional = totalAbonosPorDetalle.getOrDefault(dp.getId(), 0.0);
            dp.setMontoPagado(adicional);
        }
    }

    public void limpiarTodo() {
        abonos.clear();
        conceptos.clear();
        contadorIdTemporal = 1;
        if (tablaModelo != null) tablaModelo.limpiarTabla();
    }

    public boolean eliminarConceptoPorIdTemporal(int idTemp) {
        Iterator<DetallesPago> it = conceptos.iterator();
        while (it.hasNext()) {
            DetallesPago dp = it.next();
            if (dp.getId() == idTemp) {
                it.remove();
                tablaModelo.eliminarElementoPorIdTemporal(dp.getId());
                return true;
            }
        }
        return false; // No encontrado
    }

    public boolean eliminarAbonoPorIdTemporal(int idTemp) {
        Iterator<Abono> it = abonos.iterator();
        Abono abonoEliminado = null;

        while (it.hasNext()) {
            Abono ab = it.next();
            if (ab.getIdAbono() == idTemp) {
                abonoEliminado = ab;
                it.remove();
                tablaModelo.eliminarElementoPorIdTemporal(idTemp);
                break;
            }
        }

        if (abonoEliminado == null) return false;

        // Buscar el DetallesPago enlazado por idTemporal
        int idDetalleVinculado = abonoEliminado.getIdDetallesPago();

        // Verificar si hay otros abonos que apuntan al mismo DetallesPago
        boolean hayMasAbonos = abonos.stream()
                .anyMatch(a -> a.getIdDetallesPago() == idDetalleVinculado);

        if (hayMasAbonos) {
            // Restar el monto del abono eliminado al DetallesPago
            for (DetallesPago dp : conceptos) {
                if (dp.getId() == idDetalleVinculado) {
                    dp.setMontoPagado(dp.getMontoPagado() - abonoEliminado.getMontoAbonado());
                    break;
                }
            }
        } else {
            // Ya no hay más abonos → eliminar el DetallesPago completo
            eliminarConceptoPorIdTemporal(idDetalleVinculado);
        }

        return true;
    }

    public boolean eliminarPorIdTemporal(Object objeto) {
        if (objeto instanceof DetallesPago dp) {
            return eliminarConceptoPorIdTemporal(dp.getId());
        }
        if (objeto instanceof Abono ab) {
            return eliminarAbonoPorIdTemporal(ab.getIdAbono());
        }
        return false;
    }

}
