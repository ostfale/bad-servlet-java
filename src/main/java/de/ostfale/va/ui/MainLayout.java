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

import static com.vaadin.flow.component.icon.VaadinIcon.DASHBOARD;
import static com.vaadin.flow.component.icon.VaadinIcon.USER_HEART;

public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeaderContent();
    }

    private void createHeaderContent() {
        H1 appTitle = new H1("Bad-Stat (Java - Servlet)");
        appTitle.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.Margin.AUTO, LumoUtility.AlignContent.CENTER);

        SideNav views = getPrimaryNavigation();
        Scroller scroller = new Scroller(views);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        DrawerToggle toggle = new DrawerToggle();

        HorizontalLayout wrapper = new HorizontalLayout(toggle);
        wrapper.setAlignItems(FlexComponent.Alignment.CENTER);
        wrapper.setSpacing(false);

        VerticalLayout viewHeader = new VerticalLayout(wrapper);
        viewHeader.setPadding(false);
        viewHeader.setSpacing(false);

        addToDrawer(appTitle, scroller);
        addToNavbar(viewHeader);

        setPrimarySection(Section.DRAWER);
    }

    private SideNav getPrimaryNavigation() {
        SideNav sideNav = new SideNav();
        sideNav.addItem(
                new SideNavItem("Overview", "/" + OverviewView.PATH, DASHBOARD.create()),
                new SideNavItem("About", "/" + AboutView.PATH, USER_HEART.create())
        );
        return sideNav;
    }
}
