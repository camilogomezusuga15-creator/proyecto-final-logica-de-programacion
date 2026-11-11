package com.example.demo.views;

import com.example.demo.data.mascota;
import com.example.demo.data.Solicitud;
import com.example.demo.data.Gestordeinf;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "solicitudes", layout = mainlayout.class)
@PageTitle("Solicitudes")
public class SolicitudesView extends VerticalLayout {

    private Grid<Solicitud> grid = new Grid<>(Solicitud.class, false);
    private List<Solicitud> solicitudes;
    private List<mascota> mascotas;

    public SolicitudesView() {
        setSizeFull();
        H2 titulo = new H2("Gestión de Solicitudes");
        
        solicitudes = Gestordeinf.leerSolicitudes();
        mascotas = Gestordeinf.leerMascotas();

        grid.addColumn(Solicitud::getNombreAdoptante).setHeader("Adoptante").setSortable(true);
        grid.addColumn(Solicitud::getNombreMascota).setHeader("Mascota Solicitada").setSortable(true);
        grid.addColumn(Solicitud::getObservacion).setHeader("Observación");
        
        // Componente investigado: Badge
        grid.addComponentColumn(solicitud -> {
            Span badge = new Span(solicitud.getEstado().toUpperCase());
            if (solicitud.getEstado().equals("aprobada")) badge.getElement().getThemeList().add("badge success");
            else if (solicitud.getEstado().equals("rechazada")) badge.getElement().getThemeList().add("badge error");
            else badge.getElement().getThemeList().add("badge");
            return badge;
        }).setHeader("Estado");

        // Botones de acción
        grid.addComponentColumn(solicitud -> {
            if (!solicitud.getEstado().equals("pendiente")) {
                return new Span("Sin acciones"); // Oculta botones si ya está resuelta
            }
            
            Button btnAprobar = new Button("Aprobar", e -> aprobarSolicitud(solicitud));
            btnAprobar.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);
            
            Button btnRechazar = new Button("Rechazar", e -> rechazarSolicitud(solicitud));
            btnRechazar.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
            
            return new HorizontalLayout(btnAprobar, btnRechazar);
        }).setHeader("Acciones").setAutoWidth(true);

        grid.setItems(solicitudes);
        add(titulo, grid);
    }

    // --- LA REGLA DE NEGOCIO OBLIGATORIA ---
    private void aprobarSolicitud(Solicitud solicitudAprobada) {
        // 1. Marcar esta solicitud como aprobada
        solicitudAprobada.setEstado("aprobada");
        solicitudAprobada.setObservacion("¡Felicidades! Adopción completada.");

        // 2. Buscar la mascota y marcarla como "adoptada"
        for (mascota m : mascotas) {
            if (m.getId().equals(solicitudAprobada.getIdMascota())) {
                m.setEstado("adoptada");
                break;
            }
        }

        // 3. Buscar otras solicitudes pendientes por esta MISMA mascota y rechazarlas automáticamente
        for (Solicitud s : solicitudes) {
            if (s.getIdMascota().equals(solicitudAprobada.getIdMascota()) 
                && !s.getId().equals(solicitudAprobada.getId()) 
                && s.getEstado().equals("pendiente")) {
                
                s.setEstado("rechazada");
                s.setObservacion("Mascota adoptada por otro usuario.");
            }
        }

        // 4. Guardar todos los cambios en los archivos .txt y refrescar
        Gestordeinf.guardarSolicitudes(solicitudes);
        Gestordeinf.guardarMascotas(mascotas);
        grid.setItems(solicitudes); // Refresca la tabla
        
        Notification noti = Notification.show("Solicitud aprobada. La mascota ha sido adoptada y otras solicitudes fueron rechazadas.");
        noti.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
    
    private void rechazarSolicitud(Solicitud solicitud) {
        solicitud.setEstado("rechazada");
        solicitud.setObservacion("No cumple requisitos");
        
        Gestordeinf.guardarSolicitudes(solicitudes);
        grid.setItems(solicitudes);
    }
}
