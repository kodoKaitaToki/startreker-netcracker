package edu.netcracker.backend.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.validation.annotation.FieldMatch;
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
@FieldMatch(first = "newPassword", second = "matchNewPassword", message = "The password fields must match")
public class ChangePasswordForm {

    @NotBlank
    @Size(min = 6, max = 64)
    @JsonProperty("old_password")
    private String oldPassword;

    @NotBlank
    @Size(min = 6, max = 64)
    @JsonProperty("new_password")
    private String newPassword;

    @NotBlank
    @Size(min = 6, max = 64)
    @JsonProperty("match_new_password")
    private String matchNewPassword;

}
