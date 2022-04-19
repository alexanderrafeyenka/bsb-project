package bsb.group5.employee.service.impl;

import bsb.group5.employee.repository.EmployeeDetailsRepository;
import bsb.group5.employee.repository.EmployeeRepository;
import bsb.group5.employee.repository.model.Employee;
import bsb.group5.employee.repository.model.EmployeeDetails;
import bsb.group5.employee.service.AddEmployeeService;
import bsb.group5.employee.service.converters.EmployeeConverter;
import bsb.group5.employee.service.model.AddEmployeeDTO;
import bsb.group5.employee.service.model.ViewAddEmployeeDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AddEmployeeServiceImpl implements AddEmployeeService {
    private final EmployeeConverter employeeConverter;
    private final EmployeeRepository employeeRepository;
    private final EmployeeDetailsRepository employeeDetailsRepository;

    @Override
    @Transactional
    public ViewAddEmployeeDTO add(AddEmployeeDTO addEmployeeDTO) {
        Employee employee = employeeConverter.convert(addEmployeeDTO);
        employee.setIsHired(true);
        LocalDate recruitmentDate = addEmployeeDTO.getRecruitmentDate();
        employee.setRecruitmentDate(recruitmentDate);
        LocalDate terminationDate = addEmployeeDTO.getTerminationDate();
        employee.setTerminationDate(terminationDate);
        Employee addedEmployee = employeeRepository.persist(employee);
        EmployeeDetails employeeDetails = getEmployeeDetails(employee);
        employeeDetailsRepository.persist(employeeDetails);
        return employeeConverter.convertToViewAddDTO(addedEmployee);
    }

    private EmployeeDetails getEmployeeDetails(Employee employee) {
        EmployeeDetails details = new EmployeeDetails();
        details.setCreateDate(LocalDateTime.now());
        details.setLastUpdate(LocalDateTime.now());
        details.setEmployee(employee);
        return details;
    }
}
