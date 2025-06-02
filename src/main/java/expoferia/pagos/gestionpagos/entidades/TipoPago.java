package expoferia.pagos.gestionpagos.entidades;

public class TipoPago {

    private int id;
    private String concepto;
    private String categoria;
    private double costo;
    private boolean estado;

    public TipoPago() {}

    public TipoPago(int id, String concepto, String categoria, double costo, boolean estado) {
        this.id = id;
        this.concepto = concepto;
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

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

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
