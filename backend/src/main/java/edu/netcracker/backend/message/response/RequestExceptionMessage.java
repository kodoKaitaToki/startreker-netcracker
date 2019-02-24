package edu.netcracker.backend.message.response;

import edu.netcracker.backend.controller.exception.RequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestExceptionMessage {

    private int status;
    private String error;
    private String message;
    private long timestamp;

    public static RequestExceptionMessage createRequestExceptionMessage(RequestException exception) {
        return new RequestExceptionMessage(exception.getHttpStatus().value(),
                exception.getHttpStatus().name(),
                exception.getMessage(),
                System.currentTimeMillis());
    }
}
