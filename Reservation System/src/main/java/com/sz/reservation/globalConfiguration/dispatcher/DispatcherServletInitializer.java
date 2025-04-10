package com.sz.reservation.globalConfiguration.dispatcher;

import com.sz.reservation.accountManagement.configuration.AccountConfig;
import com.sz.reservation.globalConfiguration.RootConfig;
import com.sz.reservation.globalConfiguration.security.WebSecurityConfig;
import com.sz.reservation.propertyManagement.configuration.PropertyConfig;
import jakarta.servlet.*;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.EnumSet;

public class DispatcherServletInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // creating the root context for all dispatchers
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RootConfig.class);
        servletContext.addListener(new ContextLoaderListener(rootContext));


        AnnotationConfigWebApplicationContext defaultContext = new AnnotationConfigWebApplicationContext();
        defaultContext.register(AccountConfig.class);
        defaultContext.setParent(rootContext);

        DispatcherServlet defaultDispatcher = new DispatcherServlet(defaultContext); // defining the config class
        ServletRegistration.Dynamic firstDispatcher = servletContext.addServlet("root1Dispatcher", defaultDispatcher);
        firstDispatcher.setLoadOnStartup(2);
        firstDispatcher.addMapping("/api/v1/*");
        firstDispatcher.setMultipartConfig(new MultipartConfigElement(null,2097152,3145728,2097152)); // 2MB,3MB,2MB



        // creating the /account/listing/* dispatcher
        AnnotationConfigWebApplicationContext listingContext = new AnnotationConfigWebApplicationContext();
        listingContext.register(PropertyConfig.class); // defining the config class
        DispatcherServlet hostDispatcher = new DispatcherServlet(listingContext);
        ServletRegistration.Dynamic secondDispatcher = servletContext.addServlet("hostServlet", hostDispatcher);
        secondDispatcher.setLoadOnStartup(1);
        //secondDispatcher.setMultipartConfig(new MultipartConfigElement(null,52428800,1635778560 ,52428800)); //50MB,1560MB,50MB
        secondDispatcher.addMapping("/api/v1/host/*");






    }
}
