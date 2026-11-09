package com.example.demo.data;
import java.io.*;
import java.util.*;

public class Gestordeinf {
    private static final String FILE_PATH = "mascotas.txt";
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
    
}
