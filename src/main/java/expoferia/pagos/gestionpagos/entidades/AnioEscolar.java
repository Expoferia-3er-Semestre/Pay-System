package expoferia.pagos.gestionpagos.entidades;
import java.sql.Date;

public class AnioEscolar {
    private int idAnoEscolar;
    private Date periodoInicio;
    private Date periodoFin;
    private boolean estado; // true = activo

    // Constructor vacío
    public AnioEscolar() {
    }

    // Constructor con todos los campos
    public AnioEscolar(int idAnoEscolar, Date periodoInicio, Date periodoFin, boolean estado) {
        this.idAnoEscolar = idAnoEscolar;
        this.periodoInicio = periodoInicio;
        this.periodoFin = periodoFin;
        this.estado = estado;
    }

    // Getters y setters
    public int getIdAnoEscolar() {
        return idAnoEscolar;
    }

    public void setIdAnoEscolar(int idAnoEscolar) {
        this.idAnoEscolar = idAnoEscolar;
    }

    public Date getPeriodoInicio() {
        return periodoInicio;
    }

    public void setPeriodoInicio(Date periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    public Date getPeriodoFin() {
        return periodoFin;
    }

    public void setPeriodoFin(Date periodoFin) {
        this.periodoFin = periodoFin;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}

