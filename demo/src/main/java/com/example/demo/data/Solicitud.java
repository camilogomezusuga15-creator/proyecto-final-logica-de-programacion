package com.example.demo.data;

public class Solicitud {
    private String id;
    private String idMascota;
    private String nombreMascota;
    private String idAdoptante;
    private String nombreAdoptante;
    private String estado; // pendiente, aprobada, rechazada
    private String observacion;

    public Solicitud(String id, String idMascota, String nombreMascota, String idAdoptante, String nombreAdoptante, String estado, String observacion) {
        this.id = id;
        this.idMascota = idMascota;
        this.nombreMascota = nombreMascota;
        this.idAdoptante = idAdoptante;
        this.nombreAdoptante = nombreAdoptante;
        this.estado = estado;
        this.observacion = observacion;
    }

    public String toFileString() {
        return id + "|" + idMascota + "|" + nombreMascota + "|" + idAdoptante + "|" + nombreAdoptante + "|" + estado + "|" + observacion;
    }

    public String getId() { return id; }
    public String getIdMascota() { return idMascota; }
    public String getNombreMascota() { return nombreMascota; }
    public String getIdAdoptante() { return idAdoptante; }
    public String getNombreAdoptante() { return nombreAdoptante; }
    public String getEstado() { return estado; }
    public String getObservacion() { return observacion; }

    public void setEstado(String estado) { this.estado = estado; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}

