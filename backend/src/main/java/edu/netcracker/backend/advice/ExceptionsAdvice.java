package edu.netcracker.backend.advice;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.message.response.RequestExceptionMessage;
import edu.netcracker.backend.message.response.ValidationExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionsAdvice {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<List<ValidationExceptionResponse>> handleException(BindException ex){

        return ResponseEntity.badRequest().body(
                ex.getAllErrors()
                        .stream()
                        .map(ValidationExceptionResponse::from)
                        .collect(Collectors.toList())
        );
    }

    @ExceptionHandler(RequestException.class)
    public ResponseEntity<RequestExceptionMessage> handleException(RequestException ex, WebRequest request){
        RequestExceptionMessage exceptionMessage = new RequestExceptionMessage(
                ex.getErrorCode(),
                ex.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return ResponseEntity.badRequest().body(exceptionMessage);
    }
}
