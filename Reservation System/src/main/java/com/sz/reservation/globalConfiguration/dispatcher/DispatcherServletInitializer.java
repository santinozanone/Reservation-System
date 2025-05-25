package com.sz.reservation.globalConfiguration.dispatcher;

public class DispatcherServletInitializer /*implements WebApplicationInitializer */ {

   /* @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // creating the root context for all dispatchers
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RootConfig.class);
        servletContext.addListener(new ContextLoaderListener(rootContext));


        // creating the /api/v1/* dispatcher
        AnnotationConfigWebApplicationContext defaultContext = new AnnotationConfigWebApplicationContext();
        defaultContext.register(AccountConfig.class); // defining the config class
        defaultContext.setParent(rootContext);

        DispatcherServlet defaultDispatcher = new DispatcherServlet(defaultContext); // creating the dispatcher
        ServletRegistration.Dynamic firstDispatcher = servletContext.addServlet("DefaultDispatcher", defaultDispatcher);
        firstDispatcher.setLoadOnStartup(2); // load 2 because the other dispatcher should run first
        firstDispatcher.addMapping("/api/v1/*");
        firstDispatcher.setMultipartConfig(new MultipartConfigElement(null,2047483647,3200000000L,209715200)); // 2MB,3MB,2MB



        // creating the /api/v1/host/* dispatcher
        AnnotationConfigWebApplicationContext listingContext = new AnnotationConfigWebApplicationContext();
        listingContext.register(PropertyConfig.class); // defining the config class

        DispatcherServlet hostDispatcher = new DispatcherServlet(listingContext); // creating the dispatcher
        ServletRegistration.Dynamic secondDispatcher = servletContext.addServlet("hostDispatcher", hostDispatcher);
        secondDispatcher.setLoadOnStartup(1);
        secondDispatcher.addMapping("/api/v1/host/*");

    }

    */
}
