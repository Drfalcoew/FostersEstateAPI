package com.fostersestate.emails;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fostersestate.common.Secrets;
import com.fostersestate.emails.dto.EmailCreds;
import com.fostersestate.emails.dto.EmailRequest;
import com.fostersestate.emails.dto.EmailResponse;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.RequestScoped;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static java.util.UUID.randomUUID;


@RequestScoped
public class EmailService {

    private static final String SUPPORT_EMAIL = "support@fostersestate.com";
    private static final String NOTIFY_EMAIL = "drew@fostersestate.com";

    /**
     * Generates a random order number
     *
     * @return String orderNumber
     */
    private String generateOrderNumber() {
        return randomUUID().toString().replaceAll("[^a-zA-Z0-9]", "").substring(0, 14);
    }

    /**
     * Sends an email to the recipient
     *
     * @param emailRequest EmailRequest
     * @return String orderNumber
     */
    public EmailResponse sendEmail(EmailRequest emailRequest) {
        String orderNumber = sendEmailToRecipient(emailRequest);
        Log.debug("Sending email to self: " + emailRequest.toString());

        notifySelf(emailRequest.recipientName, orderNumber,
                emailRequest.phoneNumber, emailRequest.preferredDate, emailRequest.comments);
        return new EmailResponse(orderNumber);
    }

    /**
     * Sends an email notification to self (drew@fostersestate.com)
     *
     * @param orderNumber  Order number
     */
    private void notifySelf(String name, String orderNumber,
                            String phoneNumber, String preferredDate, String comments) {

        String _message = "Name: " + name + "\n\n" +
                "Phone Number: " + phoneNumber + "\n\n" +
                "Order Number: " + orderNumber + "\n\n" +
                "Preferred Date: " + preferredDate + "\n\n" +
                "Comments: " + comments;

        sendEmailToRecipient(new EmailRequest(name, NOTIFY_EMAIL, phoneNumber, name + " requested an appointment!",
                _message, preferredDate, comments));
    }

    /**
     * Sends an email to the specified recipient
     *
     * @param emailRequest EmailRequest
     * @return String orderNumber
     */
    private String sendEmailToRecipient(EmailRequest emailRequest) {
        Log.debug("Attempting to send email to: " + emailRequest.recipientEmail);

        String emailCredsString = Secrets.getSecret("creds/email");
        EmailCreds emailCreds;

        try {
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

        String orderNumber = generateOrderNumber();

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailCreds.getEmail(), emailCreds.getPassword());
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SUPPORT_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailRequest.recipientEmail));
            message.setSubject(emailRequest.subject + " - Order Number: " + orderNumber);
            message.setText(emailRequest.message);

            Transport.send(message);

            Log.info("Email sent successfully!");

        } catch (Exception e) {
            Log.error("Failed to send email");
            throw new RuntimeException(e);
        }
        return orderNumber;
    }
}
