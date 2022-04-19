package bsb.group5.employee.service.validators;

import bsb.group5.employee.service.model.AddEmployeeDTO;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

@Slf4j
public class EmployeeDatesValidator implements ConstraintValidator<EmployeeDates, AddEmployeeDTO> {

    @Override
    public boolean isValid(AddEmployeeDTO employeeDTO, ConstraintValidatorContext context) {
        LocalDate currentDate = LocalDate.now();
        LocalDate recruitmentDate = employeeDTO.getRecruitmentDate();
        LocalDate terminationDate = employeeDTO.getTerminationDate();
        return recruitmentDate.isBefore(currentDate) && terminationDate.isAfter(currentDate) && recruitmentDate.isBefore(terminationDate);
    }
}
