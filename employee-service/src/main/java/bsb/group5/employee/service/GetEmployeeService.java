package bsb.group5.employee.service;

import bsb.group5.employee.service.model.ViewGetEmployeeDTO;

public interface GetEmployeeService {
    ViewGetEmployeeDTO getById(Long employeeId);
}
