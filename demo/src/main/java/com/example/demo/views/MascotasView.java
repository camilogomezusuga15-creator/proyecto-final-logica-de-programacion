package com.example.demo.views;

import com.example.demo.data.mascota;
import com.example.demo.data.Gestordeinf;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "", layout = mainlayout.class) // Ruta principal
@PageTitle("Mascotas")
public class MascotasView extends VerticalLayout {

    private Grid<mascota> grid = new Grid<>(mascota.class, false);
    private TextField buscarField = new TextField();
    private ComboBox<String> filtroEstado = new ComboBox<>();
    private List<mascota> mascotas;

    public MascotasView() {
        mascotas = Gestordeinf.leerMascotas();
        setSizeFull(); //  pantalla completa

        //  Buscador y Filtro --
        buscarField.setPlaceholder("Buscar por nombre...");
        buscarField.setPrefixComponent(VaadinIcon.SEARCH.create());
        buscarField.setValueChangeMode(ValueChangeMode.LAZY);
        buscarField.addValueChangeListener(e -> actualizarGrid());

        filtroEstado.setPlaceholder("Filtrar por estado");
        filtroEstado.setItems("Todos", "disponible", "en proceso", "adoptada");
        filtroEstado.setValue("Todos");
        filtroEstado.addValueChangeListener(e -> actualizarGrid());

        HorizontalLayout toolbar = new HorizontalLayout(buscarField, filtroEstado);
        
        // -- CONFIGURACIÓN DEL GRID --
        grid.addColumn(mascota::getNombre).setHeader("Nombre").setSortable(true);
        grid.addColumn(mascota::getEspecie).setHeader("Especie");
        grid.addColumn(mascota::getRaza).setHeader("Raza");

        // COMPONENTE 1: Badge (Estado)
        grid.addComponentColumn(mascota -> {
            Span badge = new Span(mascota.getEstado().toUpperCase());
            String theme = mascota.getEstado().equals("disponible") ? "badge success" : 
                          (mascota.getEstado().equals("adoptada") ? "badge error" : "badge");
            badge.getElement().getThemeList().add(theme);
            return badge;
        }).setHeader("Estado");

        grid.addComponentColumn(mascota -> new Button("Ver Ficha", e -> abrirFichaMascota(mascota)));

        grid.setItems(mascotas);
        add(toolbar, grid);
    }

    private void actualizarGrid() {
        String texto = buscarField.getValue().toLowerCase();
        String estado = filtroEstado.getValue();

        grid.setItems(mascotas.stream()
            .filter(m -> m.getNombre().toLowerCase().contains(texto))
            .filter(m -> estado.equals("Todos") || m.getEstado().equals(estado))
            .collect(Collectors.toList()));
    }

    private void abrirFichaMascota(mascota mascota) {
        Dialog ficha = new Dialog();
        ficha.setWidth("450px");

        // COMPONENTE 2: Image
        Image foto = new Image(mascota.getFotoUrl(), "Foto de " + mascota.getNombre());
        foto.setWidth("100%");
        foto.setMaxHeight("250px");
        foto.getStyle().set("object-fit", "cover").set("border-radius", "8px");

        // COMPONENTE 3: TabSheet
        TabSheet tabSheet = new TabSheet();
        tabSheet.setWidthFull();
        
        // Pestaña 1: General
        VerticalLayout tabGeneral = new VerticalLayout(
            new Span("Nombre: " + mascota.getNombre()),
            new Span("Especie: " + mascota.getEspecie()),
            new Span("Raza: " + mascota.getRaza()),
            new Span("Edad: " + mascota.getEdad() + " años"),
            new Span("Sexo: " + mascota.getSexo())
        );
        tabGeneral.setPadding(false);

        // Pestaña 2: Médica
        VerticalLayout tabMedica = new VerticalLayout(
            new Span("Estado actual de salud:"),
            new Span(mascota.getEstadoSalud())
        );
        tabMedica.setPadding(false);
        
        // Pestaña 3: Adopción
        VerticalLayout tabAdopcion = new VerticalLayout(
            new Span("Estado de la solicitud: " + mascota.getEstado().toUpperCase())
        );
        tabAdopcion.setPadding(false);

        tabSheet.add("General", tabGeneral);
        tabSheet.add("Médica", tabMedica);
        tabSheet.add("Adopción", tabAdopcion);

        Button btnCerrar = new Button("Cerrar", e -> ficha.close());
        btnCerrar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        
        ficha.add(foto, tabSheet, btnCerrar);
        ficha.open();
    }
}
