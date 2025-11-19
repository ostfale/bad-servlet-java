package de.ostfale.va.ui.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.ostfale.va.common.UseLogging;
import de.ostfale.va.ui.MainLayout;

@Route(value = OverviewView.PATH, layout = MainLayout.class)
public class OverviewView extends VerticalLayout implements UseLogging {

    public static final String PATH = "";

    public OverviewView() {
        log().info("OverviewView :: constructor");
        setSizeFull();
        setPadding(true);
        setSpacing(true);
    }

}
