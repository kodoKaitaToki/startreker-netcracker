package edu.netcracker.backend.message.response;

import edu.netcracker.backend.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class UserResponse {

    private String username;
    private Collection<? extends GrantedAuthority> authorities;

    private UserResponse(String username, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.authorities = authorities;
    }

    public static UserResponse from(User user){
        return new UserResponse(user.getUsername(), user.getAuthorities());
    }
}
