package bsb.group5.employee.service.converters;

import bsb.group5.employee.repository.model.Employee;
import bsb.group5.employee.service.model.AddEmployeeDTO;
import bsb.group5.employee.service.model.ViewAddEmployeeDTO;
import bsb.group5.employee.service.model.ViewGetEmployeeDTO;

import java.util.List;

public interface EmployeeConverter {
    Employee convert(AddEmployeeDTO addEmployeeDTO);

    ViewAddEmployeeDTO convertToViewAddDTO(Employee employee);

    ViewGetEmployeeDTO convertToViewGetDTO(Employee employee);

    List<ViewGetEmployeeDTO> convertToViewGetDTOList(List<Employee> employees);
}
