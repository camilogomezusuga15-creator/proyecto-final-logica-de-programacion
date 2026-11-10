package com.example.demo.data;

public class Adoptante {
    private String id;
    private String nombre;
    private String telefono;
    private String tipoVivienda; // Apartamento, Casa, Finca
    private String tieneOtrasMascotas; // Si, No
    private String especiePreferencia; // Perro, Gato, Cualquiera
    private String experiencia; // Principiante, Intermedio, Experto

    public Adoptante(String id, String nombre, String telefono, String tipoVivienda, String tieneOtrasMascotas, String especiePreferencia, String experiencia) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.tipoVivienda = tipoVivienda;
        this.tieneOtrasMascotas = tieneOtrasMascotas;
        this.especiePreferencia = especiePreferencia;
        this.experiencia = experiencia;
    }

    public String toFileString() {
        return id + "|" + nombre + "|" + telefono + "|" + tipoVivienda + "|" + tieneOtrasMascotas + "|" + especiePreferencia + "|" + experiencia;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getTipoVivienda() { return tipoVivienda; }
    public String getTieneOtrasMascotas() { return tieneOtrasMascotas; }
    public String getEspeciePreferencia() { return especiePreferencia; }
    public String getExperiencia() { return experiencia; }
}

