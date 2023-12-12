package com.fostersestate.emails.dto;

import lombok.Data;

@Data
public class EmailRequest {
    public String recipientEmail;
    public String recipientName;
    public String subject;
    public String message;


}
