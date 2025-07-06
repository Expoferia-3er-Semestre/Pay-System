package expoferia.pagos.gestionpagos.entidades;

public class TipoPago {
    private int id;
    private String categoria; // "Mensualidad", "Curso", etc.
    private Double costo;
    private boolean estado;

    public TipoPago() {
    }

    public TipoPago(int id, String categoria, Double costo, boolean estado) {
        this.id = id;
        this.categoria = categoria;
        this.costo = costo;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEstado() {
        return estado;
    }

    public boolean getEstado() { return estado; }
    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
// Getters y setters
}
