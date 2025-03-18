package com.sz.reservation.GlobalExceptionHandler;

import com.sz.reservation.accountManagement.domain.exception.*;
import com.sz.reservation.accountManagement.infrastructure.exception.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSourceResolvable;
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
public class GlobalHandler extends ResponseEntityExceptionHandler {
    private Logger logger = LogManager.getLogger(GlobalHandler.class);

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

    @ExceptionHandler(value = InvalidTokenException.class)
    public ProblemDetail handleInvalidTokenException(InvalidTokenException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Invalid verification token");
        problemDetail.setDetail(exception.getMessage());
        logger.info("the token :{}  , is not valid" ,exception.getToken());
        return problemDetail;
    }

    @ExceptionHandler(value = AccountAlreadyVerifiedException.class)
    public ProblemDetail handleAccountAlreadyVerifiedException(AccountAlreadyVerifiedException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Account already verified");
        problemDetail.setDetail("The account is already verified");
        logger.info("the account with id :{}  , is already verified" ,exception.getUserId());
        return problemDetail;
    }

    @ExceptionHandler(value = MediaNotSupportedException.class )
    public ProblemDetail handleMediaNotSupportedException(MediaNotSupportedException exception){
       ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
       problemDetail.setTitle("Media type not supported");
       problemDetail.setDetail("Media type uploaded is not supported");
       logger.info(exception.getMessage(),exception);
       return problemDetail;
    }

    @ExceptionHandler(value = InvalidPhoneNumberException.class)
    public ProblemDetail handleInvalidPhoneNumberException(InvalidPhoneNumberException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Invalid phone number");
        problemDetail.setDetail("The phone number provided is not valid, CountryCode : "+ exception.getCountryCode() + " PhoneNumber: " + exception.getPhoneNumber());
        logger.info("Invalid phone number for user with email: , countryCode:{} , and phoneNumber:{}",exception.getCountryCode() , exception.getPhoneNumber());
        return problemDetail;
    }

    @ExceptionHandler(value = AccountNotEnabledException.class)
    public ProblemDetail handleAccountNotEnabledException(AccountNotEnabledException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        problemDetail.setTitle("Account not verified");
        problemDetail.setDetail("The account attempting to use the system is not verified");
        logger.info("The account trying to access the system with email:{} ,is not verified ",exception.getEmail(),exception);
        return problemDetail;
    }


    @ExceptionHandler(value = UsernameAlreadyRegisteredException.class)
    public ProblemDetail handleUserAlreadyRegisteredException(UsernameAlreadyRegisteredException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Username already registered exception");
        problemDetail.setDetail("The username is already in use");
        logger.info("error trying to insert/update user, username: {} already in use",exception.getUsername());
        return problemDetail;
    }

    @ExceptionHandler(value = EmailAlreadyRegisteredException.class)
    public ProblemDetail handleEmailAlreadyRegisteredException(EmailAlreadyRegisteredException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("email already registered exception");
        problemDetail.setDetail("The email is already use");
        logger.info("error trying to insert user, email: {} already in use",exception.getEmail());
        return problemDetail;
    }




    @ExceptionHandler(value = {FileReadingException.class, FileWritingException.class, JsonMarshalError.class,
    NetworkErrorException.class, SendGridApiException.class,LibPhoneParserException.class,FileDeletionException.class})
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
}
