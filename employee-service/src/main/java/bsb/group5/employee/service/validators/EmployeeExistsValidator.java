package bsb.group5.employee.service.validators;

import bsb.group5.employee.repository.EmployeeRepository;
import bsb.group5.employee.service.model.AddEmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmployeeExistsValidator {

    private final EmployeeRepository employeeRepository;

    public boolean isValid(AddEmployeeDTO employeeDTO) {
        boolean nameAbsent = employeeRepository.findByName(employeeDTO.getFullName()).isEmpty();
        boolean ibanBynAbsent = employeeRepository.findByIbanByn(employeeDTO.getPersonIbanByn()).isEmpty();
        boolean ibanCurrencyAbsent = employeeRepository.findByIbanCurrency(employeeDTO.getPersonIbanCurrency()).isEmpty();
        return nameAbsent && ibanBynAbsent && ibanCurrencyAbsent;
    }
}
