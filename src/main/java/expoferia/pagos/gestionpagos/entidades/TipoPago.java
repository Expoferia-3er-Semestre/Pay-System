package expoferia.pagos.gestionpagos.entidades;

public class TipoPago {

    private Integer id;
    private String concepto;
    private String categoria;
    private Double costo;
    private boolean estado;

    public TipoPago() {}

    public TipoPago(Integer id, String concepto, String categoria, Double costo, boolean estado) {
        this.id = id;
        this.concepto = concepto;
        this.categoria = categoria;
        this.costo = costo;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public boolean getEstado() { return estado; }

    public void setEstado(boolean estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "tipo_pagos{" +
                "id=" + id +
                ", concepto='" + concepto + '\'' +
                ", categoria='" + categoria + '\'' +
                ", costo=" + costo +
                '}';
    }
}
