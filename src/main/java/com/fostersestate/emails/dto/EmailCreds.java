package com.fostersestate.emails.dto;

import lombok.Data;

@Data
public class EmailCreds {
    private String email;
    private String password;

    public EmailCreds() { }

    public EmailCreds(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() { return email; }

    public String getPassword() { return password; }
}
