package com.example.demo.views;

import com.example.demo.data.mascota;
import com.example.demo.data.Gestordeinf;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.IntegerField;
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
        setSizeFull();

        // -- BARRA SUPERIOR (Buscador, Filtro y Botón Nuevo) --
        buscarField.setPlaceholder("Buscar por nombre...");
        buscarField.setPrefixComponent(VaadinIcon.SEARCH.create());
        buscarField.setValueChangeMode(ValueChangeMode.LAZY);
        buscarField.addValueChangeListener(e -> actualizarGrid());

        filtroEstado.setPlaceholder("Filtrar por estado");
        filtroEstado.setItems("Todos", "disponible", "en proceso", "adoptada");
        filtroEstado.setValue("Todos");
        filtroEstado.addValueChangeListener(e -> actualizarGrid());

        Button btnNueva = new Button("Nueva Mascota", VaadinIcon.PLUS.create(), e -> abrirFormulario(null));
        btnNueva.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout toolbar = new HorizontalLayout(buscarField, filtroEstado, btnNueva);
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        
        // -- CONFIGURACIÓN DEL GRID --
        grid.addColumn(mascota::getNombre).setHeader("Nombre").setSortable(true);
        grid.addColumn(mascota::getEspecie).setHeader("Especie");
        
        // Badge (Componente Investigado)
        grid.addComponentColumn(mascota -> {
            Span badge = new Span(mascota.getEstado().toUpperCase());
            String theme = mascota.getEstado().equals("disponible") ? "badge success" : 
                          (mascota.getEstado().equals("adoptada") ? "badge error" : "badge");
            badge.getElement().getThemeList().add(theme);
            return badge;
        }).setHeader("Estado");

        // Botones de acción en la tabla (Ver, Editar, Eliminar)
        grid.addComponentColumn(mascota -> {
            Button btnVer = new Button(VaadinIcon.EYE.create(), e -> abrirFichaMascota(mascota));
            Button btnEditar = new Button(VaadinIcon.EDIT.create(), e -> abrirFormulario(mascota));
            Button btnEliminar = new Button(VaadinIcon.TRASH.create(), e -> confirmarEliminacion(mascota));
            
            btnEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR);
            return new HorizontalLayout(btnVer, btnEditar, btnEliminar);
        }).setHeader("Acciones").setAutoWidth(true);

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

    // -- FORMULARIO DE CREACIÓN/EDICIÓN (Validaciones implementadas) --
    private void abrirFormulario(mascota mascotaEditada) {
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");
        dialog.setHeaderTitle(mascotaEditada == null ? "Nueva Mascota" : "Editar Mascota");

        FormLayout form = new FormLayout();
        TextField nombre = new TextField("Nombre *");
        TextField especie = new TextField("Especie *");
        TextField raza = new TextField("Raza");
        IntegerField edad = new IntegerField("Edad (Años) *");
        ComboBox<String> sexo = new ComboBox<>("Sexo *");
        sexo.setItems("Macho", "Hembra");
        TextField estadoSalud = new TextField("Estado de Salud");
        TextField fotoUrl = new TextField("URL de Fotografía");
        ComboBox<String> estado = new ComboBox<>("Estado *");
        estado.setItems("disponible", "en proceso", "adoptada");

        // Rellenar datos si estamos editando
        if (mascotaEditada != null) {
            nombre.setValue(mascotaEditada.getNombre());
            especie.setValue(mascotaEditada.getEspecie());
            raza.setValue(mascotaEditada.getRaza());
            edad.setValue(mascotaEditada.getEdad());
            sexo.setValue(mascotaEditada.getSexo());
            estadoSalud.setValue(mascotaEditada.getEstadoSalud());
            fotoUrl.setValue(mascotaEditada.getFotoUrl());
            estado.setValue(mascotaEditada.getEstado());
        }

        form.add(nombre, especie, raza, edad, sexo, estadoSalud, fotoUrl, estado);

        Button btnGuardar = new Button("Guardar", e -> {
            // VALIDACIÓN: Comprobar campos obligatorios
            if (nombre.isEmpty() || especie.isEmpty() || edad.isEmpty() || sexo.isEmpty() || estado.isEmpty()) {
                Notification notificacion = Notification.show("Error: Faltan campos obligatorios (*)");
                notificacion.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notificacion.setPosition(Notification.Position.MIDDLE);
                return;
            }

            // Evitar nulos en campos de texto no obligatorios
            String strRaza = raza.getValue() == null ? "No especificada" : raza.getValue();
            String strSalud = estadoSalud.getValue() == null ? "Sin registros" : estadoSalud.getValue();
            String strFoto = fotoUrl.getValue() == null || fotoUrl.isEmpty() ? "https://via.placeholder.com/300" : fotoUrl.getValue();

            if (mascotaEditada == null) {
                // Generar ID único usando el reloj del sistema para nuevos registros
                String nuevoId = String.valueOf(System.currentTimeMillis());
                mascota nueva = new mascota(nuevoId, nombre.getValue(), especie.getValue(), strRaza, 
                                            edad.getValue(), sexo.getValue(), strSalud, strFoto, estado.getValue());
                mascotas.add(nueva);
            } else {
                // Actualizar objeto existente
                int index = mascotas.indexOf(mascotaEditada);
                mascota actualizada = new mascota(mascotaEditada.getId(), nombre.getValue(), especie.getValue(), strRaza, 
                                            edad.getValue(), sexo.getValue(), strSalud, strFoto, estado.getValue());
                mascotas.set(index, actualizada);
            }

            // Persistir cambios en el .txt y refrescar pantalla
            Gestordeinf.guardarMascotas(mascotas);
            actualizarGrid();
            dialog.close();
            Notification.show("Registro guardado con éxito").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });
        
        btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button btnCancelar = new Button("Cancelar", e -> dialog.close());

        dialog.add(form);
        dialog.getFooter().add(btnCancelar, btnGuardar);
        dialog.open();
    }

    // -- CUADRO DE CONFIRMACIÓN ANTES DE BORRAR --
    private void confirmarEliminacion(mascota mascota) {
        Dialog confirmacion = new Dialog();
        confirmacion.setHeaderTitle("Confirmar Eliminación");
        confirmacion.add(new Span("¿Estás seguro de que deseas eliminar a " + mascota.getNombre() + "? Esta acción no se puede deshacer."));

        Button btnConfirmar = new Button("Sí, eliminar", e -> {
            mascotas.remove(mascota);
            Gestordeinf.guardarMascotas(mascotas); // Sobrescribe el .txt sin esa mascota
            actualizarGrid();
            confirmacion.close();
            Notification.show(mascota.getNombre() + " fue eliminado").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });
        btnConfirmar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        
        Button btnCancelar = new Button("Cancelar", e -> confirmacion.close());

        confirmacion.getFooter().add(btnCancelar, btnConfirmar);
        confirmacion.open();
    }

    // -- FICHA DETALLADA (Image y TabSheet) --
    private void abrirFichaMascota(mascota mascota) {
        Dialog ficha = new Dialog();
        ficha.setWidth("450px");

        Image foto = new Image(mascota.getFotoUrl(), "Foto de " + mascota.getNombre());
        foto.setWidth("100%");
        foto.setMaxHeight("250px");
        foto.getStyle().set("object-fit", "cover").set("border-radius", "8px");

        TabSheet tabSheet = new TabSheet();
        tabSheet.setWidthFull();
        
        VerticalLayout tabGeneral = new VerticalLayout(
            new Span("Nombre: " + mascota.getNombre()),
            new Span("Especie: " + mascota.getEspecie()),
            new Span("Raza: " + mascota.getRaza()),
            new Span("Edad: " + mascota.getEdad() + " años"),
            new Span("Sexo: " + mascota.getSexo())
        );
        tabGeneral.setPadding(false);

        VerticalLayout tabMedica = new VerticalLayout(
            new H3("Estado actual de salud:"),
            new Span(mascota.getEstadoSalud())
        );
        tabMedica.setPadding(false);
        
        VerticalLayout tabAdopcion = new VerticalLayout(
            new Span("Estado de la solicitud: " + mascota.getEstado().toUpperCase())
        );
        tabAdopcion.setPadding(false);

        tabSheet.add("General", tabGeneral);
        tabSheet.add("Médica", tabMedica);
        tabSheet.add("Adopción", tabAdopcion);

        Button btnCerrar = new Button("Cerrar", e -> ficha.close());
        ficha.add(foto, tabSheet, btnCerrar);
        ficha.open();
    }
}
