package bsb.group5.auth.service.validators;

import bsb.group5.auth.repository.UserRepository;
import bsb.group5.auth.repository.model.User;
import bsb.group5.auth.service.validators.annotations.UsernameUnique;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@AllArgsConstructor
public class UsernameUniqueValidator implements ConstraintValidator<UsernameUnique, String> {
    private final UserRepository userRepository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        Optional<User> user = userRepository.getByUsername(username);
        return user.isEmpty();
    }
}
