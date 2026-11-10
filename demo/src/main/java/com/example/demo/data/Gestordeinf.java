package com.example.demo.data;
import java.io.*;
import java.util.*;

public class Gestordeinf {
    private static final String FILE_PATH = "mascotas.txt";
    private static final String FILE_ADOPTANTES = "adoptantes.txt";
    private static final String FILE_SOLICITUDES = "solicitudes.txt";
    public static List<mascota> leerMascotas() {
        List<mascota> mascotas = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            mascotas.add(new mascota("1", "Max", "Perro", "Labrador", 3, "Macho", "Saludable, vacunas al día", "https://images.unsplash.com/photo-1552053831-71594a27632d?auto=format&fit=crop&w=300&q=80", "disponible"));
            mascotas.add(new mascota("2", "Luna", "Gato", "Siamés", 1, "Hembra", "Requiere revisión veterinaria", "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?auto=format&fit=crop&w=300&q=80", "en proceso"));
            guardarMascotas(mascotas);
            return mascotas;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length == 9) {
                    mascotas.add(new mascota(datos[0], datos[1], datos[2], datos[3], 
                        Integer.parseInt(datos[4]), datos[5], datos[6], datos[7], datos[8]));
                }
            }
        } catch (Exception e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        return mascotas;
    }
    public static void guardarMascotas(List<mascota> mascotas) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (mascota m : mascotas) {
                pw.println(m.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }
    //
     public static List<Adoptante> leerAdoptantes() {
        List<Adoptante> adoptantes = new ArrayList<>();
        File file = new File(FILE_ADOPTANTES);
        if (!file.exists()) {
            adoptantes.add(new Adoptante("A1", "Carlos Pérez", "3001234567", "Apartamento", "No", "Perro", "Principiante"));
            adoptantes.add(new Adoptante("A2", "María López", "3109876543", "Casa", "Si", "Cualquiera", "Experto"));
            guardarAdoptantes(adoptantes);
            return adoptantes;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length == 7) {
                    adoptantes.add(new Adoptante(datos[0], datos[1], datos[2], datos[3], datos[4], datos[5], datos[6]));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return adoptantes;
    }
    public static void guardarAdoptantes(List<Adoptante> adoptantes) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_ADOPTANTES))) {
            for (Adoptante a : adoptantes) pw.println(a.toFileString());
        } catch (IOException e) { e.printStackTrace(); }
    }
    //
    public static List<Solicitud> leerSolicitudes() {
        List<Solicitud> solicitudes = new ArrayList<>();
        File file = new File(FILE_SOLICITUDES);
        if (!file.exists()) {
            solicitudes.add(new Solicitud("S1", "1", "Max", "A1", "Carlos Pérez", "pendiente", "En revisión"));
            guardarSolicitudes(solicitudes);
            return solicitudes;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length == 7) {
                    solicitudes.add(new Solicitud(datos[0], datos[1], datos[2], datos[3], datos[4], datos[5], datos[6]));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return solicitudes;
    }

    public static void guardarSolicitudes(List<Solicitud> solicitudes) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_SOLICITUDES))) {
            for (Solicitud s : solicitudes) pw.println(s.toFileString());
        } catch (IOException e) { e.printStackTrace(); }
    }

    
}
