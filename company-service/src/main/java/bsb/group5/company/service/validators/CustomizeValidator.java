package bsb.group5.company.service.validators;

import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@AllArgsConstructor
public class CustomizeValidator implements ConstraintValidator<CustomizeValid, Integer> {
    private static final List<Integer> PAGES = List.of(20, 50, 100);

    @Override
    public boolean isValid(Integer page, ConstraintValidatorContext constraintValidatorContext) {
        if (page == null) {
            return true;
        } else {
            return PAGES.stream()
                    .anyMatch(page::equals);
        }
    }
}