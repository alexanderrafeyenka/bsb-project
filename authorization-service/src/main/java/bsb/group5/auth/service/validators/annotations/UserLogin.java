package bsb.group5.auth.service.validators.annotations;

import bsb.group5.auth.service.validators.UserLoginValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserLoginValidator.class)
public @interface UserLogin {
    String message() default "{message.invalid.login.password}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
