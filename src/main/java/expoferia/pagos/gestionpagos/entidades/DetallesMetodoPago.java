package expoferia.pagos.gestionpagos.entidades;

import java.time.LocalDateTime;

public class DetallesMetodoPago {
    private int id;
    private int idPagoRecibo;
    private Integer idDetallesPago; // Puede ser null si el método cubre varios conceptos
    private String metodoPago;      // Ej: "Transferencia", "Tarjeta Visa", "Efectivo"
    private double monto;
    private String referencia;
    private LocalDateTime fechaRegistro;

    // Constructor vacío
    public DetallesMetodoPago() {}

    // Constructor completo
    public DetallesMetodoPago(int id, int idPagoRecibo, Integer idDetallesPago,
                             String metodoPago, double monto, String referencia,
                             LocalDateTime fechaRegistro) {
        this.id = id;
        this.idPagoRecibo = idPagoRecibo;
        this.idDetallesPago = idDetallesPago;
        this.metodoPago = metodoPago;
        this.monto = monto;
        this.referencia = referencia;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y setters

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getIdPagoRecibo() { return idPagoRecibo; }

    public void setIdPagoRecibo(int idPagoRecibo) { this.idPagoRecibo = idPagoRecibo; }

    public Integer getIdDetallesPago() { return idDetallesPago; }

    public void setIdDetallesPago(Integer idDetallesPago) { this.idDetallesPago = idDetallesPago; }

    public String getMetodoPago() { return metodoPago; }

    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public double getMonto() { return monto; }

    public void setMonto(double monto) { this.monto = monto; }

    public String getReferencia() { return referencia; }

    public void setReferencia(String referencia) { this.referencia = referencia; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }

    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
