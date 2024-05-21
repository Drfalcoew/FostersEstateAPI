package com.fostersestate.formSubmission.dto;

import lombok.Data;
@Data
public class FormRequest {

    public String email;
    public String firstname;

    public FormRequest() { }

    public FormRequest(String firstname, String email) {
        this.email = email;
        this.firstname = email;
    }

    @Override
    public String toString() {
        return "FormRequest{" +
                "recipientEmail='" + email + '\'' +
                ", recipientName='" + firstname + '\'' +
                '}';
    }
}
