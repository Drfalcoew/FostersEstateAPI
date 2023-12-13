package com.fostersestate.emails;

import com.fostersestate.common.Secrets;
import com.fostersestate.emails.dto.EmailCreds;
import com.fostersestate.emails.dto.EmailRequest;
import com.fostersestate.emails.dto.EmailResponse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static java.util.UUID.randomUUID;

@RequestScoped
public class EmailService {

    public static final String MAIL = "drfalcoew@icloud.com";
    public static final String PASSWORD = "lvmf-yecs-etlv-hcew"; // Move to AWS Secrets Manager

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

        System.out.println("Attempting to send email to: " + emailRequest.recipientEmail);

        String emailCredsString = Secrets.getSecret("creds/email");
        EmailCreds emailCreds = new EmailCreds(emailCredsString.split(",")[0], emailCredsString.split(",")[1]);

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.mail.me.com");
        props.put("mail.smtp.port", "587");

        String orderNumber = generateOrderNumber();

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(MAIL, PASSWORD);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("support@fostersestate.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailRequest.recipientEmail));
            message.setSubject(emailRequest.subject + "Order Number: " + orderNumber);
            message.setText(emailRequest.message);

            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            System.out.println("Email failed to send!");
            throw new RuntimeException(e);
        }
        return new EmailResponse(orderNumber);
    }
}
