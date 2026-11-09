package com.example.demo.data;



public class mascota {
    private String id;
    private String nombre;
    private String especie;
    private String raza;
    private int edad;
    private String sexo;
    private String estadoSalud;
    private String fotoUrl;
    private String estado;

    public mascota(String id, String nombre, String especie, String raza, int edad, String sexo, String estadoSalud, String fotoUrl, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.edad = edad;
        this.sexo = sexo;
        this.estadoSalud = estadoSalud;
        this.fotoUrl = fotoUrl;
        this.estado = estado;
    }
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEspecie() { return especie; }
    public String getRaza() { return raza; }
    public int getEdad() { return edad; }
    public String getSexo() { return sexo; }
    public String getEstadoSalud() { return estadoSalud; }
    public String getFotoUrl() { return fotoUrl; }
    public String getEstado() { return estado; }
    
    public void setEstado(String estado) { this.estado = estado; }

}
