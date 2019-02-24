package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDTO {

    private Integer id;

    private String username;

    @JsonProperty("email")
    private String userEmail;

    @JsonProperty("telephone_number")
    private String userTelephone;

    @JsonProperty("is_activated")
    private boolean userIsActivated;

    @JsonProperty("user_created_date")
    private String userCreatedDate;

    private List<String> roles;

    private UserDTO(Integer id,
                    String username,
                    String userEmail,
                    String userTelephone,
                    boolean userIsActivated,
                    LocalDate userCreatedDate,
                    Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.userEmail = userEmail;
        this.userTelephone = userTelephone;
        this.userIsActivated = userIsActivated;
        this.userCreatedDate = userCreatedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }

    public static UserDTO from(User user) {
        return new UserDTO(user.getUserId(),
                user.getUsername(),
                user.getUserEmail(),
                user.getUserTelephone(),
                user.isUserIsActivated(),
                user.getRegistrationDate(),
                user.getAuthorities());
    }
}
