package bsb.group5.company.service.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomizeValidator.class)
public @interface CustomizeValid {
    String message() default "Parameter customized_page not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
