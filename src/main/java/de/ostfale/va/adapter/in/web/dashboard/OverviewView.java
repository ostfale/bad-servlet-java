package de.ostfale.va.adapter.in.web.dashboard;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.ostfale.va.common.UseLogging;
import de.ostfale.va.adapter.in.web.app.MainView;

@Route(value = OverviewView.PATH, layout = MainView.class)
public class OverviewView extends VerticalLayout implements UseLogging {

    public static final String PATH = "overview";

    public OverviewView() {
        log().info("OverviewView :: constructor");
        setSizeFull();
        setPadding(true);
        setSpacing(true);
    }

}
