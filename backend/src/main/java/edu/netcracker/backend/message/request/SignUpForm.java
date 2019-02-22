package edu.netcracker.backend.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.validation.annotation.FieldMatch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldMatch(first = "password", second = "matchPassword", message = "The password fields must match")
public class SignUpForm extends SignInForm {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 64)
    @JsonProperty("match_password")
    private String matchPassword;

    @NotBlank
    @JsonProperty("telephone_number")
    private String telephoneNumber;
}
