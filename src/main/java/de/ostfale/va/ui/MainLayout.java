package de.ostfale.va.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;
import de.ostfale.va.ui.views.AboutView;
import de.ostfale.va.ui.views.OverviewView;
import de.ostfale.va.ui.views.TournamentView;

import static com.vaadin.flow.component.icon.VaadinIcon.*;

public class MainLayout extends AppLayout {

    private static final String APP_TITLE = "Bad-Stat (Java - Servlet)";

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addNavbarContent();
    }

    private void addDrawerContent() {
        H1 appTitle = new H1(APP_TITLE);
        appTitle.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.Margin.AUTO, LumoUtility.AlignContent.CENTER);

        Scroller scroller = new Scroller(getPrimaryNavigation());
        scroller.setClassName(LumoUtility.Padding.SMALL);

        addToDrawer(appTitle, scroller);
    }

    private void addNavbarContent() {
        DrawerToggle toggle = new DrawerToggle();

        HorizontalLayout wrapper = new HorizontalLayout(toggle);
        wrapper.setAlignItems(FlexComponent.Alignment.CENTER);
        wrapper.setSpacing(false);

        VerticalLayout viewHeader = new VerticalLayout(wrapper);
        viewHeader.setPadding(false);
        viewHeader.setSpacing(false);

        addToNavbar(viewHeader);
    }

    private SideNav getPrimaryNavigation() {
        SideNav sideNav = new SideNav();
        sideNav.addItem(
                new SideNavItem("Overview", "/" + OverviewView.PATH, DASHBOARD.create()),
                new SideNavItem("Tournaments", "/" + TournamentView.PATH, LINES_LIST.create()),
                new SideNavItem("About", "/" + AboutView.PATH, USER_HEART.create())
        );
        return sideNav;
    }
}
