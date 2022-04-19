package bsb.group5.employee.service.impl;

import bsb.group5.employee.config.MessageConfig;
import bsb.group5.employee.repository.EmployeeRepository;
import bsb.group5.employee.repository.model.Employee;
import bsb.group5.employee.service.GetAllEmployeeService;
import bsb.group5.employee.service.converters.EmployeeConverter;
import bsb.group5.employee.service.model.PaginationDTO;
import bsb.group5.employee.service.model.PaginationEnum;
import bsb.group5.employee.service.model.ViewGetEmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class GetAllEmployeeServiceImpl implements GetAllEmployeeService {
    private final EmployeeRepository employeeRepository;
    private final MessageConfig messageConfig;
    private final EmployeeConverter employeeConverter;

    @Override
    @Transactional
    public List<ViewGetEmployeeDTO> getAll(PaginationDTO paginationDTO) {
        List<Employee> employees;
        PaginationEnum pagination = paginationDTO.getPagination();
        switch (pagination) {
            case Default:
                int maxResult = Integer.parseInt(messageConfig.getPaginationMaxDefault());
                Integer startIndexDefault = getStartIndex(paginationDTO.getPage(), maxResult);
                employees = employeeRepository.findAll(
                        startIndexDefault,
                        maxResult
                );
                log.info(employees.toString());
                break;
            case Customized:
                Integer startIndexCustomized = getStartIndex(paginationDTO.getPage(), paginationDTO.getCustomizedPage());
                employees = employeeRepository.findAll(
                        startIndexCustomized,
                        paginationDTO.getCustomizedPage()
                );
                log.info(employees.toString());
                break;
            default:
                employees = employeeRepository.findAll(null, null);
                log.info(employees.toString());
                break;
        }
        List<ViewGetEmployeeDTO> viewGetEmployeeDTOS = employeeConverter.convertToViewGetDTOList(employees);
        log.info(viewGetEmployeeDTOS.toString());
        return viewGetEmployeeDTOS;
    }

    private Integer getStartIndex(Integer startIndex, Integer maxResult) {
        return (startIndex - 1) * maxResult;
    }
}
