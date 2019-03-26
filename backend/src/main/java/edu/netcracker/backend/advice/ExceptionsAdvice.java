package edu.netcracker.backend.advice;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.message.response.RequestExceptionMessage;
import edu.netcracker.backend.message.response.ValidationExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.List;

@RestControllerAdvice
public class ExceptionsAdvice {

    @ExceptionHandler(RequestException.class)
    public ResponseEntity<RequestExceptionMessage> handleException(RequestException ex){
        return  ResponseEntity.
                status(ex.getHttpStatus()).
                body(RequestExceptionMessage.createRequestExceptionMessage(ex));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RequestExceptionMessage> handleException(MethodArgumentNotValidException ex){

        Class errorClass = ex.getParameter().getNestedParameterType();

        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ValidationExceptionMessage.
                createValidationExceptionMessage(fieldErrors, errorClass));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<RequestExceptionMessage> handleException(ConstraintViolationException ex){
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(requestExceptionMessage(ex,
                HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<RequestExceptionMessage> handleException(BadCredentialsException ex){
        return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(requestExceptionMessage(ex,
                HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RequestExceptionMessage> handleException(AccessDeniedException ex){
        return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(requestExceptionMessage(ex,
                HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<RequestExceptionMessage> handleException(LockedException ex){
        return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(requestExceptionMessage(ex,
                HttpStatus.UNAUTHORIZED));
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<RequestExceptionMessage> handleException(Exception ex){
//        RequestExceptionMessage message = new RequestExceptionMessage();
//        message.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        message.setError(HttpStatus.INTERNAL_SERVER_ERROR.name());
//        message.setTimestamp(System.currentTimeMillis());
//        message.setMessage("Something went wrong");
//
//        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
//    }

    private RequestExceptionMessage requestExceptionMessage(Exception e, HttpStatus httpStatus) {
        RequestExceptionMessage message = new RequestExceptionMessage();
        message.setStatus(httpStatus.value());
        message.setError(httpStatus.name());
        message.setTimestamp(System.currentTimeMillis());
        message.setMessage(e.getMessage());

        return message;
    }
}
