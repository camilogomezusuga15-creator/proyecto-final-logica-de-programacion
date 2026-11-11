package com.example.demo.views;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

public class mainlayout extends AppLayout {
    public mainlayout() {
        crearHeader();
        crearMenuDrawer();
    }

    private void crearHeader() {
        H1 logo = new H1("Refugio de Mascotas");
        logo.getStyle().set("font-size", "1.5em").set("margin", "0");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }

    private void crearMenuDrawer() {
        // Aquí agregarás los RouterLink de las otras vistas (Adoptantes, Solicitudes)
        RouterLink mascotasLink = new RouterLink("Catálogo de Mascotas", MascotasView.class);
        RouterLink adoptantesLink = new RouterLink("Adoptantes", AdoptantesView.class);
        RouterLink solicitudesLink = new RouterLink("Solicitudes de Adopción", SolicitudesView.class);
        
        VerticalLayout menu = new VerticalLayout(mascotasLink, adoptantesLink, solicitudesLink);
        addToDrawer(menu);
    }
}
