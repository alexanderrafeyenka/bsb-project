package bsb.group5.employee.service.impl;

import bsb.group5.employee.config.MessageConfig;
import bsb.group5.employee.repository.EmployeeRepository;
import bsb.group5.employee.repository.model.Employee;
import bsb.group5.employee.service.GetEmployeeService;
import bsb.group5.employee.service.converters.EmployeeConverter;
import bsb.group5.employee.service.exceptions.EmployeeGetException;
import bsb.group5.employee.service.model.ViewGetEmployeeDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class GetEmployeeServiceImpl implements GetEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeConverter employeeConverter;
    private final MessageConfig messageConfig;

    @Override
    @Transactional
    public ViewGetEmployeeDTO getById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId);
        if (employee == null) {
            throw new EmployeeGetException(messageConfig.getMessageEmployeeNotExists());
        }
        return employeeConverter.convertToViewGetDTO(employee);
    }
}
