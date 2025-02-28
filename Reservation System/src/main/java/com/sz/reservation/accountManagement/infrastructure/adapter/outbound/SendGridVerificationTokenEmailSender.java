package com.sz.reservation.accountManagement.infrastructure.adapter.outbound;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import com.sz.reservation.accountManagement.domain.port.outbound.VerificationTokenEmailSender;
import com.sz.reservation.accountManagement.infrastructure.exception.JsonMarshalError;
import com.sz.reservation.accountManagement.infrastructure.exception.NetworkErrorException;
import com.sz.reservation.accountManagement.infrastructure.exception.SendGridApiException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Profile("prod")
public class SendGridVerificationTokenEmailSender implements VerificationTokenEmailSender {
    private Logger logger = LogManager.getLogger(SendGridVerificationTokenEmailSender.class);

    @Value("${SENDGRID.APIKEY}")
    private String API_KEY;


    private final int ACCEPTED_202 = 202;

    @Override
    public void sendEmailTo(String email,String username ,String token)  {
        logger.debug("starting sending email to: {} , with username:{} , and token: {} ",email,username,token);

        Email from = new Email("reservation.notificationsender@gmail.com");
        Email to = new Email(email);

        String subject = "Account Verification token from Reservation System";

        Content content = new Content("text/html", "value");
        Personalization personalization = new Personalization();
        personalization.addTo(to);
        personalization.addDynamicTemplateData("subject",subject);
        personalization.addDynamicTemplateData("username",username);
        personalization.addDynamicTemplateData("token",token);


        Mail mail = new Mail();
        mail.setFrom(from);
        mail.addContent(content);
        mail.addPersonalization(personalization);
        mail.setTemplateId("d-2cf97d64b6c8495c9e22d1a7b49c00cb");


        SendGrid sg = new SendGrid(API_KEY);
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        try {
            request.setBody(mail.build());
            logger.info("Email sended successfully to :{} ",email);
        } catch (IOException e) {
            logger.error("error while parsing json for sending email to:{} , with username:{} , and token: {} ",email,username,token);
            throw new JsonMarshalError(e);
        }
        try {
            Response response = sg.api(request);
            if (response.getStatusCode() != ACCEPTED_202){
                logger.error("error returned from the SendGrid api, status code:{}",response.getStatusCode());
                throw new SendGridApiException(response.getBody());
            }
        } catch (IOException e) {
            logger.error("Network error when sending email to:{} ",email);
            throw new NetworkErrorException("There was a network error while sending email",e);
        }
    }
}
