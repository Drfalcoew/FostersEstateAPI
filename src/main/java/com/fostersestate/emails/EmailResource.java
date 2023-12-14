package com.fostersestate.emails;

import com.fostersestate.emails.dto.EmailRequest;
import com.fostersestate.emails.dto.EmailResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.net.UnknownServiceException;

@Path("/email")
public class EmailResource {

    @Inject
    EmailService emailService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        System.out.println("Hello from RESTEasy Reactive");
        return "Hello from RESTEasy Reactive";
    }

    /**
     * This method is used to send an email.
     * @param emailRequest - the email request object
     * @return - the order number
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("*/*")
    public EmailResponse sendEmail(EmailRequest emailRequest) throws UnknownServiceException {
        System.out.println("Email Request: " + emailRequest.toString());
        try {
            return emailService.sendEmail(emailRequest);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownServiceException("Failed to send email");
        }
    }
}
