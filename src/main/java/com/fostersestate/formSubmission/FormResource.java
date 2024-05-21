package com.fostersestate.formSubmission;

import com.fostersestate.formSubmission.dto.FormRequest;
import io.quarkus.logging.Log;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.UnknownServiceException;
import java.util.Arrays;

@Path("/form")
public class FormResource {
    private final FormService formService = new FormService();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendEmail(FormRequest formRequest) throws UnknownServiceException {

        Log.info("Received email request: " + formRequest.toString());
        try {
            formService.sendEmail(formRequest);
            String accessControlAllowOriginValue = "https://drfalcoew.com";

            return Response
                    .ok()
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

