package com.fostersestate.emails.dto;

import lombok.Data;

@Data
public class EmailRequest {
    public String recipientEmail;
    public String recipientName;
    public String phoneNumber;
    public String subject;
    public String message;

    public String preferredDate;

    public String comments;

    public EmailRequest() { }

    public EmailRequest(String name, String recipientEmail, String phoneNumber,
                        String subject, String message, String preferredDate, String comments) {
        this.recipientEmail = recipientEmail;
        this.recipientName = name;
        this.phoneNumber = phoneNumber;
        this.subject = subject;
        this.message = message;
        this.preferredDate = preferredDate;
        this.comments = comments;
    }


}
