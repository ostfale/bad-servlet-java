package de.ostfale.va.adapter.in.web.app;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = AboutView.PATH, layout = MainView.class)
public class AboutView extends VerticalLayout {

    public static final String PATH = "about";

    public AboutView() {
        H1 title = new H1("About");
        H2 subtitle = new H2("Badminton App - Vaadin Servlet");

        Paragraph version = new Paragraph("Version: 1.12");
        Paragraph author = new Paragraph("Created by: Uwe Sauerbrei");

        setSpacing(true);
        setPadding(true);
        setAlignItems(Alignment.CENTER);

        add(title, subtitle, version, author);
    }
}
