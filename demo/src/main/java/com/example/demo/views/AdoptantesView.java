package com.example.demo.views;

import com.example.demo.data.Adoptante;
import com.example.demo.data.mascota;
import com.example.demo.data.Gestordeinf;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Route(value = "adoptantes", layout = mainlayout.class)
@PageTitle("Adoptantes")
public class AdoptantesView extends VerticalLayout {

    private Grid<Adoptante> grid = new Grid<>(Adoptante.class, false);
    private List<Adoptante> adoptantes;
    private List<mascota> mascotasBase; // Para calcular compatibilidad

    public AdoptantesView() {
        setSizeFull();
        H2 titulo = new H2("Registro de Adoptantes");

        adoptantes = Gestordeinf.leerAdoptantes();
        mascotasBase = Gestordeinf.leerMascotas();

        grid.addColumn(Adoptante::getNombre).setHeader("Nombre").setSortable(true);
        grid.addColumn(Adoptante::getTipoVivienda).setHeader("Vivienda");
        grid.addColumn(Adoptante::getExperiencia).setHeader("Experiencia");
        grid.addColumn(Adoptante::getEspeciePreferencia).setHeader("Preferencia");

        // BONIFICACIÓN: Botón para calcular compatibilidad
        grid.addComponentColumn(adoptante -> {
            Button btnRecomendar = new Button("Ver Recomendadas", e -> calcularYMostrarRecomendaciones(adoptante));
            btnRecomendar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            return btnRecomendar;
        }).setHeader("Compatibilidad (+0.5)").setAutoWidth(true);

        grid.setItems(adoptantes);
        add(titulo, grid);
    }

    // --- ALGORITMO DE BONIFICACIÓN (0 a 100) ---
    private void calcularYMostrarRecomendaciones(Adoptante adoptante) {
        // Clase interna temporal para guardar la mascota y su puntaje juntos
        class Recomendacion implements Comparable<Recomendacion> {
            mascota mascota;
            int puntaje;

            Recomendacion(mascota m, int p) { this.mascota = m; this.puntaje = p; }

            // Para ordenar de mayor a menor puntaje
            @Override
            public int compareTo(Recomendacion otra) {
                return Integer.compare(otra.puntaje, this.puntaje);
            }
        }

        List<Recomendacion> listaRecomendadas = new ArrayList<>();

        for (mascota mascota : mascotasBase) {
            // Solo evaluar mascotas que estén disponibles
            if (!mascota.getEstado().equals("disponible")) continue;

            int puntaje = 0;

            // REGLA 1: Preferencia directa (40 puntos)
            if (adoptante.getEspeciePreferencia().equalsIgnoreCase(mascota.getEspecie()) || 
                adoptante.getEspeciePreferencia().equalsIgnoreCase("Cualquiera")) {
                puntaje += 40;
            }

            // REGLA 2: Vivienda vs Tamaño/Especie (30 puntos)
            if (adoptante.getTipoVivienda().equalsIgnoreCase("Finca") || adoptante.getTipoVivienda().equalsIgnoreCase("Casa")) {
                puntaje += 30; // Mucho espacio sirve para cualquiera
            } else if (adoptante.getTipoVivienda().equalsIgnoreCase("Apartamento")) {
                if (mascota.getEspecie().equalsIgnoreCase("Gato") || mascota.getRaza().toLowerCase().contains("pequeño")) {
                    puntaje += 30; // Apartamentos son ideales para gatos o razas pequeñas
                } else {
                    puntaje += 10; // Penalización por perro grande en apartamento
                }
            }

            // REGLA 3: Experiencia vs Edad de la mascota (30 puntos)
            if (adoptante.getExperiencia().equalsIgnoreCase("Experto")) {
                puntaje += 30; // Puede manejar cachorros o ancianos
            } else if (adoptante.getExperiencia().equalsIgnoreCase("Intermedio")) {
                puntaje += 25;
            } else { // Principiante
                if (mascota.getEdad() > 1 && mascota.getEdad() < 7) {
                    puntaje += 30; // Mascota adulta joven es más fácil para un principiante
                } else {
                    puntaje += 15; // Cachorros (muy inquietos) o muy mayores requieren más experiencia
                }
            }

            // Asegurar límites (0-100)
            if (puntaje > 100) puntaje = 100;
            if (puntaje < 0) puntaje = 0;

            listaRecomendadas.add(new Recomendacion(mascota, puntaje));
        }

        // Ordenar la lista (el método compareTo lo hace de mayor a menor)
        Collections.sort(listaRecomendadas);

        // --- MOSTRAR RESULTADO EN UN DIALOG ---
        Dialog dialogResultados = new Dialog();
        dialogResultados.setWidth("600px");
        dialogResultados.setHeaderTitle("Mascotas recomendadas para " + adoptante.getNombre());

        VerticalLayout contenido = new VerticalLayout();
        
        if (listaRecomendadas.isEmpty()) {
            contenido.add(new Span("No hay mascotas disponibles en este momento."));
        } else {
            // Un mini Grid temporal solo para mostrar los puntajes
            Grid<Recomendacion> gridRec = new Grid<>();
            gridRec.setItems(listaRecomendadas);
            gridRec.addColumn(r -> r.mascota.getNombre()).setHeader("Nombre de la Mascota");
            gridRec.addColumn(r -> r.mascota.getEspecie() + " " + r.mascota.getRaza()).setHeader("Detalles");
            
            // Columna de puntaje con colores
            gridRec.addComponentColumn(r -> {
                Span badge = new Span(r.puntaje + " / 100");
                if (r.puntaje >= 80) badge.getElement().getThemeList().add("badge success");
                else if (r.puntaje >= 50) badge.getElement().getThemeList().add("badge");
                else badge.getElement().getThemeList().add("badge error");
                return badge;
            }).setHeader("Puntaje de Compatibilidad").setSortable(true);

            contenido.add(gridRec);
        }

        Button btnCerrar = new Button("Cerrar", e -> dialogResultados.close());
        dialogResultados.add(contenido);
        dialogResultados.getFooter().add(btnCerrar);
        dialogResultados.open();
    }
}
