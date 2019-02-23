package edu.netcracker.backend.message.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateForm extends UserForm {

    @NotNull
    private Integer userId;
}
