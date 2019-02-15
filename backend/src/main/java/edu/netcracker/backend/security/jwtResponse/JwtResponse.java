package edu.netcracker.backend.security.jwtResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type;
    private String username;
    private Collection<? extends GrantedAuthority> authorities;
}
