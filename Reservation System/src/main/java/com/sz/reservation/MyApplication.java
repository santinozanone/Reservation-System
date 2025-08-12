package com.sz.reservation;

import com.sz.reservation.accountManagement.configuration.AccountConfig;
import com.sz.reservation.globalConfiguration.RootConfig;
import com.tngtech.archunit.core.domain.JavaClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.modulith.Modulith;
import org.springframework.modulith.core.ApplicationModules;



@Modulith
public class MyApplication    {
    private static Logger logger = LogManager.getLogger(MyApplication.class);

    public static void main(String[] args) {

       var modules =  ApplicationModules.
                of(MyApplication.class, JavaClass.Predicates.
                        //excluding security configuration from verification
                        resideInAPackage("com.sz.reservation.globalConfiguration.security.."))
                .verify();


        new SpringApplicationBuilder().
                properties("spring.config.location=classpath:/spring.properties,classpath:/account.properties,classpath:/listing.properties")
                .profiles("test","default")
                .sources(RootConfig.class)
                .run();


    }
}
