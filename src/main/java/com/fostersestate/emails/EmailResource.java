package com.fostersestate.emails;

import com.fostersestate.emails.dto.EmailRequest;
import com.fostersestate.emails.dto.EmailResponse;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.net.UnknownServiceException;
import java.util.Arrays;

@Path("/email")
public class EmailResource {

    @Inject
    EmailService emailService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        Log.debug("Hello from RESTEasy Reactive");
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
        Log.debug("Recieved email request: " + emailRequest.toString());
        try {
            return emailService.sendEmail(emailRequest);
        } catch (Exception e) {
            Log.error("Failed to send email: " + e.getMessage());
            Log.error(Arrays.toString(e.getStackTrace()));
            throw new UnknownServiceException("Failed to send email");
        }
    }
}
