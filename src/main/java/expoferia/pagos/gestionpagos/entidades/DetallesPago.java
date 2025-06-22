    package expoferia.pagos.gestionpagos.entidades;

    public class DetallesPago {
        private int idDetallesPago;
        private int idAnoEscolar;
        private String idPago;
        private int idTipoPago;
        private String mesCorrespondiente;
        private String descripcion;
        private double abono;
        private double montoAbonado;

        // Constructor completo
        public DetallesPago(int idDetallesPago, int idAnoEscolar, String idPago, int idTipoPago,
                            String mesCorrespondiente, String descripcion, double abono, double montoAbonado) {
            this.idDetallesPago = idDetallesPago;
            this.idAnoEscolar = idAnoEscolar;
            this.idPago = idPago;
            this.idTipoPago = idTipoPago;
            this.mesCorrespondiente = mesCorrespondiente;
            this.descripcion = descripcion;
            this.abono = abono;
            this.montoAbonado = montoAbonado;
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

        public double getAbono() { return abono; }
        public void setAbono(double abono) { this.abono = abono; }

        public double getMontoAbonado() { return montoAbonado; }
        public void setMontoAbonado(double montoAbonado) { this.montoAbonado = montoAbonado; }
    }

