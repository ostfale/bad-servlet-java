package de.ostfale.va;

import com.vaadin.flow.server.ErrorHandler;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinServletService;
import de.ostfale.va.common.UseLogging;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;


@WebServlet(urlPatterns = "/*", name = "Custom AppServlet", asyncSupported = true, loadOnStartup = 1)
public class AppServlet extends VaadinServlet implements UseLogging {

    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();
        log().info("AppServlet :: servletInitialized");
        configureSessionInitListener();
    }

    private void configureSessionInitListener() {
        VaadinServletService service = getService();
        service.addSessionInitListener(event ->
                event.getSession().setErrorHandler(createSessionErrorHandler())
        );
    }

    private ErrorHandler createSessionErrorHandler() {
        return errorEvent -> {
            Throwable throwable = errorEvent.getThrowable();
            log().error("Session error occurred", throwable);
        };
    }
}
