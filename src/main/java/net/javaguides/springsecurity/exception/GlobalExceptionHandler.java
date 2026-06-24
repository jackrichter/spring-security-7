package net.javaguides.springsecurity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice               // Obs! Combines @ControllerAdvice and @ResponseBody, which means the response is JSON !!!
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleUsernameNotFoundException(
            UsernameNotFoundException ex,
            WebRequest webRequest) {

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(LocalDateTime.now());
        errorDetails.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorDetails.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetails.setPath(webRequest.getDescription(false));     // This returns the URL path
        errorDetails.setMessage(ex.getMessage());

        // This returns a JSON response with the error details
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex,
            WebRequest webRequest) {

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(LocalDateTime.now());
        errorDetails.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorDetails.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetails.setPath(webRequest.getDescription(false));
        errorDetails.setMessage(ex.getMessage());

        // This returns a JSON response with the error details
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
