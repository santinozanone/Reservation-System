package com.sz.reservation;

import com.sz.reservation.accountManagement.configuration.AccountConfig;
import com.sz.reservation.globalConfiguration.RootConfig;
import com.sz.reservation.listingManagement.configuration.ListingConfig;
import com.tngtech.archunit.core.domain.JavaClass;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.modulith.ApplicationModule;
import org.springframework.modulith.Modulith;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

//@Configuration
//@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
//@ComponentScan(basePackages = "com.sz.reservation.boot")

@Modulith
public class MyApplication    {

    public static void main(String[] args) {
      /* var modules =  ApplicationModules.
                of(MyApplication.class, JavaClass.Predicates.resideInAPackage("com.sz.reservation.globalConfiguration.security.."));
              //  .verify();

        new Documenter(modules)
                .writeModulesAsPlantUml()
                .writeIndividualModulesAsPlantUml();
*/

      new SpringApplicationBuilder()
                .parent(RootConfig.class).web(WebApplicationType.NONE)
                .child(AccountConfig.class).properties("spring.config.name=account")
                .profiles("test","default").web(WebApplicationType.SERVLET)

           //   .sibling(ListingConfig.class).profiles("test","default").properties("spring.config.name=listing").web(WebApplicationType.SERVLET)
               .run(args);





 /*   @Bean
    public ServletRegistrationBean rootServlet(){
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        AnnotationConfigWebApplicationContext defaultContext = new AnnotationConfigWebApplicationContext();
        defaultContext.register(AccountConfig.class); // defining the config class

        dispatcherServlet.setApplicationContext(defaultContext);
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(dispatcherServlet, "/api/v1/*");
        servletRegistrationBean.setName("api-v1");
        return servletRegistrationBean;
    }

    @Bean
    public ServletRegistrationBean hostServlet(){
        DispatcherServlet hostDispatcher = new DispatcherServlet(); // creating the dispatcher
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(PropertyConfig.class); // defining the config class

        hostDispatcher.setApplicationContext(dispatcherContext);
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(hostDispatcher, "/api/v1/host/*");
        servletRegistrationBean.setName("api-v1-host");
        return servletRegistrationBean;
    }
*/

}
}
