package com.fostersestate.emails.dto;

public class EmailResponse {
    public String orderNumber;

    public EmailResponse(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public String toString() {
        return "EmailResponse{" +
                "orderNumber='" + orderNumber + '\'' +
                '}';
    }
}