package com.example.studenttutormatchapp.remote.response;

import java.util.ArrayList;

public class ErrorResponse {

    private int statusCode;
    private ArrayList<String> message = new ArrayList<>();
    private String error;

    public ErrorResponse(int statusCode, ArrayList<String> message, String error) {
        this.statusCode = statusCode;
        this.message = message;
        this.error = error;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public ArrayList<String> getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "statusCode=" + statusCode +
                ", message=" + message +
                ", error='" + error + '\'' +
                '}';
    }
}
