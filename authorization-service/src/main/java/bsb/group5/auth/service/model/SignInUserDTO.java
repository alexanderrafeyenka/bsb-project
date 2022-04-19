package bsb.group5.auth.service.model;

import bsb.group5.auth.service.validators.annotations.EmailUnique;
import bsb.group5.auth.service.validators.annotations.UsernameUnique;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode
public class SignInUserDTO {
    @UsernameUnique
    @Pattern(
            regexp = "[a-z]{6,100}",
            message = "{message.pattern.username}"
    )
    private final String username;
    @Pattern(
            regexp = "(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{8,20}",
            message = "{message.pattern.password}"
    )
    private final String password;
    @Size(max = 100, message = "{message.length.password}")
    @EmailUnique
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",
            message = "{message.pattern.email}")
    private final String email;
    @Pattern(regexp = "[А-ЯЁа-яё]{1,20}", message = "{message.pattern.firstname}")
    private final String firstName;
}