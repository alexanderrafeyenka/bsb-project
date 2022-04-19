package bsb.group5.auth.service.validators;

import bsb.group5.auth.repository.UserRepository;
import bsb.group5.auth.repository.model.User;
import bsb.group5.auth.service.validators.annotations.EmailUnique;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@AllArgsConstructor
public class EmailUniqueValidator implements ConstraintValidator<EmailUnique, String> {
    private final UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        Optional<User> user = userRepository.getByUserMail(email);
        return user.isEmpty();
    }
}
