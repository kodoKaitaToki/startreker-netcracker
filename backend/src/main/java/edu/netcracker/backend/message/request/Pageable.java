package edu.netcracker.backend.message.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
public class Pageable {

    @Min(0)
    private Long offset = 0L;

    @Min(1)
    private Long limit = 5L;
}
