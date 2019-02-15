package edu.netcracker.backend.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInformationHolder {

    private String username;
    private String password;
    private List<String> roles;

    public static UserInformationHolder createUserInformationHolder(UserDetails userDetails) {
        UserInformationHolder userInformationHolder = new UserInformationHolder();
        userInformationHolder.setUsername(userDetails.getUsername());
        userInformationHolder.setPassword(userDetails.getPassword());
        userInformationHolder.setRoles(userDetails.getAuthorities().
                stream().
                map(GrantedAuthority::getAuthority).
                collect(Collectors.toList()));

        return userInformationHolder;
    }
}
