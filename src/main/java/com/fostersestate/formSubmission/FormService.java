package com.fostersestate.formSubmission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fostersestate.common.Secrets;
import com.fostersestate.emails.dto.EmailCreds;
import com.fostersestate.formSubmission.dto.FormRequest;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import javax.mail.*;
import javax.mail.Authenticator;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


@ApplicationScoped
public class FormService {

    private static final String SUPPORT_EMAIL = "support@fostersestate.com";

    /**
     * Sends an email to the recipient
     *
     * @param formRequest FormRequest
     */
    public void sendEmail(FormRequest formRequest) {
        Log.info("Sending email: " + formRequest.toString());
        sendEmailToRecipient(formRequest);
        Log.info("Sending email to self: " + formRequest);
    }

    /**
     * Sends an email to the specified recipient
     *
     * @param formRequest FormRequest
     */
    private void sendEmailToRecipient(FormRequest formRequest) {
        Log.info("Attempting to send email from " + SUPPORT_EMAIL + " to " + formRequest.email);

        String emailCredsString = Secrets.getSecret("creds/email");
        EmailCreds emailCreds;

        try {
            Log.info("Parsing email creds");
            ObjectMapper objectMapper = new ObjectMapper();
            emailCreds = objectMapper.readValue(emailCredsString, EmailCreds.class);
        } catch (Exception e) {
            Log.error("Failed to parse email creds");
            throw new RuntimeException(e);
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.mail.me.com");
        props.put("mail.smtp.port", "587");

        Log.info("Creating email session");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailCreds.getEmail(), emailCreds.getPassword());
                    }
                });

        try {

            Log.info("Creating email message");
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SUPPORT_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(formRequest.email));
            message.setSubject("Hi " + formRequest.firstname + ", thank you for your submission!");
            message.setText("Thank you for your submission! This is a test email. Please ignore. \n- Drew Foster");

            Log.info("Sending email");
            Transport.send(message);

            Log.info("Email sent successfully!");

        } catch (Exception e) {
            Log.error("Failed to send email");
            throw new RuntimeException(e);
        }
    }
}
