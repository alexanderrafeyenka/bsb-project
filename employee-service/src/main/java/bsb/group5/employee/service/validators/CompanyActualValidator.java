package bsb.group5.employee.service.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CompanyActualValidator implements ConstraintValidator<CompanyActual, String> {

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return true;
    }
}
