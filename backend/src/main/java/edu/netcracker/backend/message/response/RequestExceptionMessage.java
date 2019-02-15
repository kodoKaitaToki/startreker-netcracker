package edu.netcracker.backend.message.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestExceptionMessage {

    private int code;
    private String message;
    private String currentTime;
}
