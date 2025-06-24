package expoferia.pagos.gestionpagos.entidades;

import java.util.List;
import expoferia.pagos.gestionpagos.entidades.Abono;

public class DetallesPago {
    private int idDetallesPago;
    private int idAnoEscolar;
    private String idPago;
    private int idTipoPago;
    private String mesCorrespondiente;
    private String descripcion;

    private double montoEsperado; // Cuota que se espera pagar (extraído de tipo_pago)
    private List<Abono> abonos;   // Lista de abonos relacionados

    public DetallesPago(int idDetallesPago, int idAnoEscolar, String idPago, int idTipoPago,
                        String mesCorrespondiente, String descripcion) {
        this.idDetallesPago = idDetallesPago;
        this.idAnoEscolar = idAnoEscolar;
        this.idPago = idPago;
        this.idTipoPago = idTipoPago;
        this.mesCorrespondiente = mesCorrespondiente;
        this.descripcion = descripcion;
    }

    // Getters y setters
    public int getIdDetallesPago() { return idDetallesPago; }
    public void setIdDetallesPago(int idDetallesPago) { this.idDetallesPago = idDetallesPago; }

    public int getIdAnoEscolar() { return idAnoEscolar; }
    public void setIdAnoEscolar(int idAnoEscolar) { this.idAnoEscolar = idAnoEscolar; }

    public String getIdPago() { return idPago; }
    public void setIdPago(String idPago) { this.idPago = idPago; }

    public int getIdTipoPago() { return idTipoPago; }
    public void setIdTipoPago(int idTipoPago) { this.idTipoPago = idTipoPago; }

    public String getMesCorrespondiente() { return mesCorrespondiente; }
    public void setMesCorrespondiente(String mesCorrespondiente) { this.mesCorrespondiente = mesCorrespondiente; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getMontoEsperado() { return montoEsperado; }
    public void setMontoEsperado(double montoEsperado) { this.montoEsperado = montoEsperado; }

    public List<Abono> getAbonos() { return abonos; }
    public void setAbonos(List<Abono> abonos) { this.abonos = abonos; }

    // Total abonado calculado desde la lista
    public double getTotalAbonado() {
        if (abonos == null) return 0.0;
        return abonos.stream()
                .mapToDouble(Abono::getMontoAbonado)
                .sum();
    }

    // Evalúa si el total abonado es exactamente igual al esperado
    public boolean isPagado() {
        return getTotalAbonado() == montoEsperado;
    }

    // Retorna el estado descriptivo del abono
    public String getEstado() {
        double total = getTotalAbonado();

        if (total == 0.0) {
            return "Pendiente";
        } else if (total < montoEsperado) {
            return "Abonando";
        } else {
            return "Pagado";
        }
    }
}



