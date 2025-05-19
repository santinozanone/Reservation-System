package com.sz.reservation.accountManagement.configuration.exceptionHandler;

import com.sz.reservation.accountManagement.domain.exception.*;
import com.sz.reservation.accountManagement.infrastructure.exception.JsonMarshalError;
import com.sz.reservation.accountManagement.infrastructure.exception.LibPhoneParserException;
import com.sz.reservation.accountManagement.infrastructure.exception.NetworkErrorException;
import com.sz.reservation.accountManagement.infrastructure.exception.SendGridApiException;
import com.sz.reservation.globalConfiguration.exception.FileDeletionException;
import com.sz.reservation.globalConfiguration.exception.MediaNotSupportedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AccountExceptionHandler {
    private Logger logger = LogManager.getLogger(AccountExceptionHandler.class);

    @ExceptionHandler(value = {JsonMarshalError.class,
            NetworkErrorException.class, SendGridApiException.class, LibPhoneParserException.class})
    public ProblemDetail handleInfrastructureExceptions(Exception exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Internal server error");
        problemDetail.setDetail("An internal server error has occurred");
        logger.error(exception.getMessage(),exception);
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
        problemDetail.setDetail("The email is already in use");
        logger.info("error trying to insert user, email: {} already in use",exception.getEmail());
        return problemDetail;
    }

}
