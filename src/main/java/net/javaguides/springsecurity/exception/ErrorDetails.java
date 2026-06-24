package net.javaguides.springsecurity.exception;

import java.time.LocalDateTime;

/*
 * To be able to handle the error response in JSON format because of @ResponseBody baked in @RestControllerAdvice
 */
public class ErrorDetails {

    private LocalDateTime timestamp;
    private String error;
    private int status;
    private String message;
    private String path;

    public ErrorDetails() {
    }

    public ErrorDetails(LocalDateTime timestamp, String error, int status, String message, String path) {
        this.timestamp = timestamp;
        this.error = error;
        this.status = status;
        this.message = message;
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
