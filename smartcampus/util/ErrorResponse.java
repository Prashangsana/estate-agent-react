/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.util;

/**
 * A simple, consistent JSON error payload returned by every ExceptionMapper.
 *
 * Example response body:
 * {
 *   "status":  409,
 *   "error":   "Conflict",
 *   "message": "Room LIB-301 cannot be deleted because it still has 2 sensor(s) assigned."
 * }
 */
public class ErrorResponse {

    private int    status;
    private String error;
    private String message;

    public ErrorResponse() {}

    public ErrorResponse(int status, String error, String message) {
        this.status  = status;
        this.error   = error;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
