package com.sz.reservation.globalConfiguration.globalExceptionHandler;

import com.sz.reservation.globalConfiguration.exception.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;

import org.springframework.validation.method.MethodValidationException;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalHandler extends ResponseEntityExceptionHandler {
    private Logger logger = LogManager.getLogger(GlobalHandler.class);


    @ExceptionHandler(value = InvalidRequestException.class)
    public ProblemDetail handleInvalidRequestTypeException(InvalidRequestException exception){
        ProblemDetail problemDetail =  ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Invalid Request");
        problemDetail.setDetail(exception.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Bad Request");
        ArrayList<String> validationErrors = new ArrayList<>();

        for(ConstraintViolation violation: ex.getConstraintViolations()){
            validationErrors.add(violation.getMessage());
        }
        problemDetail.setDetail("Invalid Parameters " + validationErrors);
        logger.info("request contain invalid parameters:{}",validationErrors);
        return new ResponseEntity<Object>(problemDetail, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException e){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        problemDetail.setTitle("Access denied");
        problemDetail.setDetail("this account does not have permission to access the specified resource");
        return problemDetail;
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException e){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setTitle("Invalid credentials");
        problemDetail.setDetail("invalid email or password");
        return problemDetail;
    }

    @ExceptionHandler(value = {FileReadingException.class, FileWritingException.class,FileDeletionException.class, DirectoryCreationException.class})
    public ProblemDetail handleInfrastructureExceptions(Exception exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Internal server error");
        problemDetail.setDetail("An internal server error has occurred");
        logger.error(exception.getMessage(),exception);
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
        logger.info("request from: {} , contain invalid parameters:{}",request.getUserPrincipal(),validationErrors);
        return new ResponseEntity<Object>(problemDetail, HttpStatus.BAD_REQUEST);

    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                   HttpStatusCode status, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setDetail("The server could not find the specified resource");

        logger.info("404 Bad Resource for request:'{}'", request);
        return new ResponseEntity<Object>(problemDetail, HttpStatus.NOT_FOUND);
    }

    // Handle NoResourceFoundException (e.g., static content)
    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers,
                                                                    HttpStatusCode status, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setDetail("The server could not find the specified resource");
        logger.info("404 Bad Resource for request:'{}'", request);
        return new ResponseEntity<Object>(problemDetail, HttpStatus.NOT_FOUND);
    }


    @Override
    protected ResponseEntity<Object> handleMethodValidationException(MethodValidationException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Bad Request");
        ArrayList<String> validationErrors = new ArrayList<>();
        for(ParameterValidationResult p: ex.getParameterValidationResults()){
            for (MessageSourceResolvable m:p.getResolvableErrors()){
                validationErrors.add(m.getDefaultMessage());
            }
        }
        problemDetail.setDetail("Invalid Parameters " + validationErrors);
        logger.info("request from : {} , contain invalid parameters:{}",request.getUserPrincipal(),validationErrors);
        return new ResponseEntity<Object>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Bad Request");
        problemDetail.setDetail("missing path variable");
        return new ResponseEntity<Object>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Bad Request");
        problemDetail.setDetail("missing servlet request parameter");
        return new ResponseEntity<Object>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Bad Request");
        problemDetail.setDetail("missing servlet request multipart parameter");
        return new ResponseEntity<Object>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    //Handle all remaining errors
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setDetail("The server could not handle this request");
        logger.error("Unexpected error caused by:{} , produced by request :{} ",ex,request);
        return new ResponseEntity<Object>(problemDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
