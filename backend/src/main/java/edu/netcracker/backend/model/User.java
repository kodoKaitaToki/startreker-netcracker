package edu.netcracker.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class User {

    private String name;

    private String surname;
}
