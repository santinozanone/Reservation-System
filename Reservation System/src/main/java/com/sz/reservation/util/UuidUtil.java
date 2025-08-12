package com.sz.reservation.util;

import com.github.f4b6a3.uuid.UuidCreator;
import com.github.f4b6a3.uuid.util.UuidValidator;
import org.springframework.stereotype.Component;

@Component
public class UuidUtil {
    private final static int VERSION_7 = 7;

    public static String createUUIDV7(){
        return UuidCreator.getTimeOrderedEpoch().toString();
    }

    public static boolean isUuidV7Valid(String uuidV7){
        return UuidValidator.isValid(uuidV7,VERSION_7);
    }



}
