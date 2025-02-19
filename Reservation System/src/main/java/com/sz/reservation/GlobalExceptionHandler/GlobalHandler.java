package com.sz.reservation.GlobalExceptionHandler;

import com.sz.reservation.registration.domain.exception.AccountNotEnabledException;
import com.sz.reservation.registration.domain.exception.EmailAlreadyRegisteredException;
import com.sz.reservation.registration.domain.exception.MediaNotSupportedException;
import com.sz.reservation.registration.domain.exception.UsernameAlreadyRegisteredException;
import com.sz.reservation.registration.infrastructure.exception.*;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = MediaNotSupportedException.class )
    public ProblemDetail handleMediaNotSupported(){
       ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
       problemDetail.setTitle("Media type not supported");
       problemDetail.setDetail("Media type uploaded is not supported");
       //TODO: LOG THE ERROR
       return problemDetail;
    }



    @ExceptionHandler(value = AccountNotEnabledException.class)
    public ProblemDetail handleAccountNotEnabledException(AccountNotEnabledException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        problemDetail.setTitle("Account not verified");
        problemDetail.setDetail("The account attempting to use the system is not verified");
        //TODO: LOG THE ERROR
        return problemDetail;
    }


    @ExceptionHandler(value = UsernameAlreadyRegisteredException.class)
    public ProblemDetail handleUserAlreadyRegisteredException(UsernameAlreadyRegisteredException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Username already registered exception");
        problemDetail.setDetail("The username is already use");
        //TODO: LOG THE ERROR
        return problemDetail;
    }

    @ExceptionHandler(value = EmailAlreadyRegisteredException.class)
    public ProblemDetail handleEmailAlreadyRegisteredException(EmailAlreadyRegisteredException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("email already registered exception");
        problemDetail.setDetail("The email is already use");
        //TODO: LOG THE ERROR
        return problemDetail;
    }



    //@ExceptionHandler(value = {DirectoryCreationException.class, FileReadingException.class, FileWritingException.class, JsonMarshalError.class,
   // NetworkErrorException.class, SendGridApiException.class})
    @ExceptionHandler(value = FileWritingException.class)
    public ProblemDetail handleInfrastructureExceptions(FileWritingException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Internal server error");
        problemDetail.setDetail("An internal server error has occurred");
        System.out.println(exception.getCause().toString());
        System.out.println(exception.getMessage());
        System.out.println(Arrays.toString(exception.getStackTrace()));
        //TODO: LOG THE ERROR
        return problemDetail;
    }



    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Bad Request");
        Map<String, String> validationErrors = new HashMap<>();
        for (FieldError e : ex.getFieldErrors()) {
            validationErrors.put(e.getField(), e.getDefaultMessage());
        }
        problemDetail.setDetail("Invalid Parameters " + validationErrors);
        return new ResponseEntity<Object>(problemDetail, HttpStatus.BAD_REQUEST);

    }

    // Handle NoHandlerFoundException (e.g., An inexistent controller uri)
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                   HttpStatusCode status, WebRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setDetail("The server could not find the specified resource");
        //TODO : LOG
       // logger.info("404 Bad Resource for request:'{}'", request);
        return new ResponseEntity<Object>(problemDetail, HttpStatus.NOT_FOUND);
    }

    // Handle NoResourceFoundException (e.g., static content)
    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers,
                                                                    HttpStatusCode status, WebRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setDetail("The server could not find the specified resource");
        //TODO : LOG
        return new ResponseEntity<Object>(problemDetail, HttpStatus.NOT_FOUND);
    }

    //Handle all remaining errors
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setDetail("The server could not handle this request");
        //TODO : LOG
        System.out.println(ex.getCause().toString());
        return new ResponseEntity<Object>(problemDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
