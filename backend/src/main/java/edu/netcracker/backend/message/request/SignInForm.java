package edu.netcracker.backend.message.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignInForm {

    @NotBlank
    @Size(min = 3, max = 24)
    private String username;

    @NotBlank
    @Size(min = 6, max = 64)
    private String password;
}
