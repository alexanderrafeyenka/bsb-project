package bsb.group5.auth.service.validators.annotations;

import bsb.group5.auth.service.validators.EmailUniqueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailUniqueValidator.class)
public @interface EmailUnique {
    String message() default "{message.not.unique}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
