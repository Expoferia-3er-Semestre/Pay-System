package expoferia.pagos.gestionpagos.entidades;

import expoferia.pagos.gestionpagos.dao.TipoPagoDAO;

public class DetallesPago {
    private int id;
    private int idPagoRecibo;
    private int idTipoPago;
    private String numTrans;       // opcional, según tipo de pago
    private int idAnoEscolar;
    private String descripcion;
    private String mesCorrespondiente;
    private double montoTotal;
    private double montoPagado;

    public DetallesPago() {
    }

    public DetallesPago(int id, int idPagoRecibo, int idTipoPago, String numTrans, int idAnoEscolar, String descripcion, String mesCorrespondiente, double montoTotal, double montoPagado) {
        this.id = id;
        this.idPagoRecibo = idPagoRecibo;
        this.idTipoPago = idTipoPago;
        this.numTrans = numTrans;
        this.idAnoEscolar = idAnoEscolar;
        this.descripcion = descripcion;
        this.mesCorrespondiente = mesCorrespondiente;
        this.montoTotal = montoTotal;
        this.montoPagado = montoPagado;
    }

    // Getters y setters

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPagoRecibo() {
        return idPagoRecibo;
    }

    public void setIdPagoRecibo(int idPagoRecibo) {
        this.idPagoRecibo = idPagoRecibo;
    }

    public int getIdTipoPago() {
        return idTipoPago;
    }

    public void setIdTipoPago(int idTipoPago) {
        this.idTipoPago = idTipoPago;
    }

    public String getNumTrans() {
        return numTrans;
    }

    public void setNumTrans(String numTrans) {
        this.numTrans = numTrans;
    }

    public int getIdAnoEscolar() {
        return idAnoEscolar;
    }

    public void setIdAnoEscolar(int idAnoEscolar) {
        this.idAnoEscolar = idAnoEscolar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMesCorrespondiente() {
        return mesCorrespondiente;
    }

    public void setMesCorrespondiente(String mesCorrespondiente) {
        this.mesCorrespondiente = mesCorrespondiente;
    }

    public String getCategoria() {

        TipoPagoDAO tipoPagoDAO = new TipoPagoDAO();
        TipoPago tipoPago = tipoPagoDAO.buscarPorId(idTipoPago);
        return tipoPago.getCategoria();

    }
}
