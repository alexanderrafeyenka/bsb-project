package bsb.group5.auth.service.validators;

import bsb.group5.auth.service.model.LoginUserDTO;
import bsb.group5.auth.service.validators.annotations.UserLogin;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserLoginValidator implements ConstraintValidator<UserLogin, LoginUserDTO> {

    @Override
    public boolean isValid(LoginUserDTO loginUserDTO, ConstraintValidatorContext constraintValidatorContext) {
        String username = loginUserDTO.getUsername();
        String userMail = loginUserDTO.getUserMail();
        String password = loginUserDTO.getPassword();
        if (username != null && userMail != null) {
            return false;
        } else if (username == null && userMail == null) {
            return false;
        } else {
            return !password.isBlank();
        }
    }
}
