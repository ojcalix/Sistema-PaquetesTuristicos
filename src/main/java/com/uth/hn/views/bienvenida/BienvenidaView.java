package com.uth.hn.views.bienvenida;

import com.uth.hn.views.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

@PageTitle("Bienvenida")
@Route(value = "bienvenida", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class BienvenidaView extends VerticalLayout {

    public BienvenidaView() {
        setSpacing(false);

        Image img = new Image("https://www.entornoturistico.com/wp-content/uploads/2020/09/agencia-de-viajes-1024x594.jpg", "placeholder plant");
        img.setWidth("500px");
        add(img);

        H2 header = new H2("Sistema de paquetes Turisticos");
        header.addClassNames(Margin.Top.XLARGE, Margin.Bottom.MEDIUM);
        add(header);
        add(new Paragraph("Tu aplicacion ideal para que puedas reservas con nosotros"));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
