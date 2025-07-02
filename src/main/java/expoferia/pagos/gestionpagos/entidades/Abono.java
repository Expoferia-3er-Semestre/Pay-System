package expoferia.pagos.gestionpagos.entidades;

import java.time.LocalDate;

public class Abono {
    private int idAbono;
    private int idDetallesPago;
    private LocalDate fechaAbono;
    private double montoAbonado;
    private String descripcion;
    private String metodoPago;
    private String numTrans;

    public Abono() {
    }

    public Abono(int idAbono, int idDetallesPago, LocalDate fechaAbono, double montoAbonado, String descripcion, String metodoPago, String numTrans) {
        this.idAbono = idAbono;
        this.idDetallesPago = idDetallesPago;
        this.fechaAbono = fechaAbono;
        this.montoAbonado = montoAbonado;
        this.descripcion = descripcion;
        this.metodoPago = metodoPago;
        this.numTrans = numTrans;
    }

    // Getters y setters
    public int getIdAbono() { return idAbono; }
    public void setIdAbono(int idAbono) { this.idAbono = idAbono; }

    public int getIdDetallesPago() { return idDetallesPago; }
    public void setIdDetallesPago(int idDetallesPago) { this.idDetallesPago = idDetallesPago; }

    public LocalDate getFechaAbono() { return fechaAbono; }
    public void setFechaAbono(LocalDate fechaAbono) { this.fechaAbono = fechaAbono; }

    public double getMontoAbonado() { return montoAbonado; }
    public void setMontoAbonado(double montoAbonado) { this.montoAbonado = montoAbonado; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getMetodoPago() {
        return metodoPago;
    }
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getNumTrans() {
        return numTrans;
    }
    public void setNumTrans(String numTrans) {
        this.numTrans = numTrans;
    }
}
