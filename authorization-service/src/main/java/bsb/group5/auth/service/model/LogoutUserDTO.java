package bsb.group5.auth.service.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Pattern;

@Data
@EqualsAndHashCode
public class LogoutUserDTO {
    @JsonCreator
    public LogoutUserDTO(String username) {
        this.username = username;
    }

    @Pattern(message = "Username is invalid", regexp = "[a-z]{6,100}")
    private final String username;
}
