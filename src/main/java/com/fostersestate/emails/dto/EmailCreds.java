package com.fostersestate.emails.dto;

import lombok.Data;

@Data
public class EmailCreds {
    private String email;
    private String password;

    public EmailCreds(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "EmailCreds{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
