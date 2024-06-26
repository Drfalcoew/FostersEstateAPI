package com.fostersestate.emails;

import com.fostersestate.emails.dto.EmailRequest;
import com.fostersestate.emails.dto.EmailResponse;
import io.quarkus.logging.Log;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.UnknownServiceException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Path("/email")
public class EmailResource {

    private final EmailService emailService = new EmailService();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendEmail(EmailRequest emailRequest) throws UnknownServiceException {

        Log.info("Received email request: " + emailRequest.toString());
        try {
            EmailResponse emailResponse = emailService.sendEmail(emailRequest);
            String accessControlAllowOriginValue = "https://fostersestate.com";

            return Response
                    .ok(emailResponse)
                    .header("Access-Control-Allow-Origin", accessControlAllowOriginValue)
                    .header("Access-Control-Allow-Methods", "POST, OPTIONS")
                    .header("Access-Control-Max-Age", "3600")
                    .header("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,x-requested-with")
                    .build();
        } catch (Exception e) {
            System.out.print("Failed to send email: " + e.getMessage());
            System.out.print(Arrays.toString(e.getStackTrace()));
            throw new UnknownServiceException("UNKNOWN ERROR. Failed to send email");
        }
    }

}

