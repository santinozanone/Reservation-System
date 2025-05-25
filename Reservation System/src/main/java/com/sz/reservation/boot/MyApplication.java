package com.sz.reservation.boot;

import com.sz.reservation.accountManagement.configuration.AccountConfig;
import com.sz.reservation.globalConfiguration.RootConfig;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

//@Configuration
//@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
//@ComponentScan(basePackages = "com.sz.reservation.boot")
public class MyApplication    {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .parent(RootConfig.class).web(WebApplicationType.NONE)
                .child(AccountConfig.class).profiles("test","default").web(WebApplicationType.SERVLET)
                //.sibling(PropertyConfig.class).web(WebApplicationType.SERVLET)
                .run(args);
    }



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
