package com.sz.reservation.architecture;

import com.sz.reservation.MyApplication;
import com.tngtech.archunit.core.domain.JavaClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;


@DisplayName("Modulith Architecture Test")
public class ModulithArchitectureTest {

    @Test
    public void Should_VerifyArchitecture_Correctly(){
        ApplicationModules.
                of(MyApplication.class, JavaClass.Predicates.
                        //excluding security configuration from verification
                                resideInAPackage("com.sz.reservation.globalConfiguration.security.."))
                .verify();
    }
}
