package expoferia.pagos.gestionpagos.entidades;

import java.sql.Date;

public class PagoRecibo {
    private int idPagoRecibo;
    private int idEstudiante;
    private double montoTotal;
    private double montoPagado;
    private boolean estado; // true = pagado, false = parcial/pending
    private Date fechaPago;

    public PagoRecibo() {
    }

    public PagoRecibo(int idPagoRecibo, int idEstudiante, double montoTotal, double montoPagado, boolean estado, Date fechaPago) {
        this.idPagoRecibo = idPagoRecibo;
        this.idEstudiante = idEstudiante;
        this.montoTotal = montoTotal;
        this.montoPagado = montoPagado;
        this.estado = estado;
        this.fechaPago = fechaPago;
    }

    public int getIdPagoRecibo() {
        return idPagoRecibo;
    }

    public void setIdPagoRecibo(int idPagoRecibo) {
        this.idPagoRecibo = idPagoRecibo;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public double getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(double montoPagado) {
        this.montoPagado = montoPagado;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

// Getters y setters
}
