package expoferia.pagos.gestionpagos.entidades;

import java.sql.Date;

public class Estudiante {
    private int id;
    private String cedulaRep;
    private String nombre1;
    private String nombre2;
    private String apellido1;
    private String apellido2;
    private Date fechaN;
    private String direccion;
    private Boolean estado;
    private String grado;
    private String nivelAcademico;

    // Constructor vacío
    public Estudiante() {}

    // Constructor completo
    public Estudiante(int id, String cedulaRep, String nombre1, String nombre2,
                      String apellido1, String apellido2, Date fechaN,
                      String direccion, Boolean estado, String grado, String nivelAcademico) {
        this.id = id;
        this.cedulaRep = cedulaRep;
        this.nombre1 = nombre1;
        this.nombre2 = nombre2;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.fechaN = fechaN;
        this.direccion = direccion;
        this.estado = estado;
        this.grado = grado;
        this.nivelAcademico = nivelAcademico;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCedulaRep() { return cedulaRep; }
    public void setCedulaRep(String cedulaRep) { this.cedulaRep = cedulaRep; }

    public String getNombre1() { return nombre1; }
    public void setNombre1(String nombre1) { this.nombre1 = nombre1; }

    public String getNombre2() { return nombre2; }
    public void setNombre2(String nombre2) { this.nombre2 = nombre2; }

    public String getApellido1() { return apellido1; }
    public void setApellido1(String apellido1) { this.apellido1 = apellido1; }

    public String getApellido2() { return apellido2; }
    public void setApellido2(String apellido2) { this.apellido2 = apellido2; }

    public Date getFechaN() { return fechaN; }
    public void setFechaN(Date fechaN) { this.fechaN = fechaN; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    public String getGrado() { return grado; }
    public void setGrado(String grado) { this.grado = grado; }

    public String getNivelAcademico() { return nivelAcademico; }
    public void setNivelAcademico(String nivelAcademico) { this.nivelAcademico = nivelAcademico; }
}
