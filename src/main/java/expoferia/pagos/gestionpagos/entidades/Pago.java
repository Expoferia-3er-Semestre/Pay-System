package expoferia.pagos.gestionpagos.entidades;

import java.sql.Date;

public class Pago {
    private int id;
    private int idEstudiante;
    private Date fecha;
    private double montoTotal;
    private String estado;
    private String metodoPago;

    // Constructor
    public Pago(int id, int idEstudiante, Date fecha, double montoTotal, String estado, String metodoPago) {
        this.id = id;
        this.idEstudiante = idEstudiante;
        this.fecha = fecha;
        this.montoTotal = montoTotal;
        this.estado = estado;
        this.metodoPago = metodoPago;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante = idEstudiante; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(double montoTotal) { this.montoTotal = montoTotal; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
}
