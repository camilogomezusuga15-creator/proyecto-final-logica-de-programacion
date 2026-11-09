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
        addToNavbar(new DrawerToggle());
        addToNavbar(new H1("Mi Aplicación"));
    }
}
