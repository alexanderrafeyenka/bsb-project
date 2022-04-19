package bsb.group5.employee.service;

import bsb.group5.employee.service.model.AddEmployeeDTO;
import bsb.group5.employee.service.model.ViewAddEmployeeDTO;

public interface AddEmployeeService {
    ViewAddEmployeeDTO add(AddEmployeeDTO addEmployeeDTO);
}
