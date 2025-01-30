package com.sz.reservation.registration.exception.handler;

import com.sz.reservation.registration.exception.MediaNotSupportedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(value = MediaNotSupportedException.class )
    public ProblemDetail handleMediaNotSupported(){
       ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
       problemDetail.setTitle("Media type not supported");
       problemDetail.setDetail("Media type uploaded is not supported");
       return problemDetail;
    }

}
