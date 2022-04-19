package bsb.group5.employee.service.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CompanyActualValidator.class)
public @interface CompanyActual {

    String message() default "${message.invalid.parameters}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
