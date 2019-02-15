package edu.netcracker.backend.message.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm extends UserForm {

    @Email
    private String email;
    @NotNull
    private Boolean isCarrier;
}
