package edu.netcracker.backend.message.response;

import lombok.Getter;
import org.springframework.validation.ObjectError;

@Getter
public class ValidationExceptionResponse {
    private String inputname;
    private String message;

    public static ValidationExceptionResponse from(ObjectError error){
        ValidationExceptionResponse ex = new ValidationExceptionResponse();
        ex.inputname = error.getObjectName();
        ex.message = error.getDefaultMessage();
        return ex;
    }
}
