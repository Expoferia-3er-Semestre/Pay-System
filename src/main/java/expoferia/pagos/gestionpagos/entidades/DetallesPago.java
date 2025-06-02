package expoferia.pagos.gestionpagos.entidades;

public class DetallesPago {
    private int id;
    private int idPago;
    private int idTipoPago;
    private String descripcion;
    private boolean abono;
    private double montoAbonado;

    // Constructor
    public DetallesPago(int id, int idPago, int idTipoPago, String descripcion, boolean abono, double montoAbonado) {
        this.id = id;
        this.idPago = idPago;
        this.idTipoPago = idTipoPago;
        this.descripcion = descripcion;
        this.abono = abono;
        this.montoAbonado = montoAbonado;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdPago() { return idPago; }
    public void setIdPago(int idPago) { this.idPago = idPago; }

    public int getIdTipoPago() { return idTipoPago; }
    public void setIdTipoPago(int idTipoPago) { this.idTipoPago = idTipoPago; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public boolean isAbono() { return abono; }
    public void setAbono(boolean abono) { this.abono = abono; }

    public double getMontoAbonado() { return montoAbonado; }
    public void setMontoAbonado(double montoAbonado) { this.montoAbonado = montoAbonado; }
}
