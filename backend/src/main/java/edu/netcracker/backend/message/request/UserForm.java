package edu.netcracker.backend.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {

    @NotBlank
    @Size(min = 3, max = 24)
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @JsonProperty("telephone_number")
    private String telephoneNumber;

    @NotNull
    @JsonProperty("is_activated")
    private Boolean isActivated;
}
