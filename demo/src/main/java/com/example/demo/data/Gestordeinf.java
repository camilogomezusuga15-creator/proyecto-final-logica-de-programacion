package com.example.demo.data;
import java.io.*;
import java.util.*;

public class Gestordeinf {
    private static final String FILE_PATH = "mascotas.txt";
    public static List<mascota> leerMascotas() {
        List<mascota> mascotas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 9) {
                    mascota mascota = new mascota(
                            datos[0], // id
                            datos[1], // nombre
                            datos[2], // especie
                            datos[3], // raza
                            Integer.parseInt(datos[4]), // edad
                            datos[5], // sexo
                            datos[6], // estadoSalud
                            datos[7], // fotoUrl
                            datos[8]  // estado
                    );
                    mascotas.add(mascota);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mascotas;
    }
    
}
