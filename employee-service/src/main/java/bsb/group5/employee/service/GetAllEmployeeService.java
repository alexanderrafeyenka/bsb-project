package bsb.group5.employee.service;

import bsb.group5.employee.service.model.PaginationDTO;
import bsb.group5.employee.service.model.ViewGetEmployeeDTO;

import java.util.List;

public interface GetAllEmployeeService {
    List<ViewGetEmployeeDTO> getAll(PaginationDTO paginationDTO);
}
