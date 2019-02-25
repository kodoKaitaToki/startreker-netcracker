package edu.netcracker.backend.message.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class MandatoryTimeInterval {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate from;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate to;
}
