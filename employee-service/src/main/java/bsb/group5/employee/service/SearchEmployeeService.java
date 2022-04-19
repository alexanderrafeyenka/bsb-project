package bsb.group5.employee.service;

import bsb.group5.employee.service.model.EmployeeSearchDTO;
import bsb.group5.employee.service.model.PaginationDTO;
import bsb.group5.employee.service.model.ViewGetEmployeeDTO;

import java.util.List;

public interface SearchEmployeeService {

    List<ViewGetEmployeeDTO> search(EmployeeSearchDTO companySearchDTO, PaginationDTO paginationDTO);
}
