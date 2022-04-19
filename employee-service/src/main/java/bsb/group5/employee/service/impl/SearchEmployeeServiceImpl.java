package bsb.group5.employee.service.impl;

import bsb.group5.employee.config.MessageConfig;
import bsb.group5.employee.repository.CompanyFeignRepository;
import bsb.group5.employee.repository.EmployeeRepository;
import bsb.group5.employee.repository.model.CompanyDTO;
import bsb.group5.employee.repository.model.Employee;
import bsb.group5.employee.service.SearchEmployeeService;
import bsb.group5.employee.service.converters.EmployeeConverter;
import bsb.group5.employee.service.model.EmployeeSearchDTO;
import bsb.group5.employee.service.model.PaginationDTO;
import bsb.group5.employee.service.model.PaginationEnum;
import bsb.group5.employee.service.model.ViewGetEmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SearchEmployeeServiceImpl implements SearchEmployeeService {
    private final CompanyFeignRepository companyFeignRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeConverter employeeConverter;
    private final MessageConfig messageConfig;

    @Override
    @Transactional
    public List<ViewGetEmployeeDTO> search(EmployeeSearchDTO companySearchDTO, PaginationDTO paginationDTO) {
        List<CompanyDTO> companyDTOList = companyFeignRepository.search(
                companySearchDTO.getNameLegal(),
                companySearchDTO.getUnp()
        );
        List<String> nameLegals = getNameLegalsList(companyDTOList);
        List<Employee> employees;
        PaginationEnum pagination = paginationDTO.getPagination();

        switch (pagination) {
            case Default:
                int maxResult = Integer.parseInt(messageConfig.getPaginationMaxDefault());
                Integer startIndexDefault = getStartIndex(paginationDTO.getPage(), maxResult);
                employees = employeeRepository.findByNameLegals(
                        nameLegals,
                        companySearchDTO.getFullName(),
                        startIndexDefault,
                        maxResult);
                break;
            case Customized:
                Integer startIndexCustomized = getStartIndex(paginationDTO.getPage(), paginationDTO.getCustomizedPage());
                employees = employeeRepository.findByNameLegals(
                        nameLegals,
                        companySearchDTO.getFullName(),
                        startIndexCustomized,
                        paginationDTO.getCustomizedPage()
                );
                break;
            default:
                employees = employeeRepository.findByNameLegals(
                        nameLegals,
                        companySearchDTO.getFullName(),
                        null,
                        null);
                break;
        }
        List<ViewGetEmployeeDTO> viewGetEmployeeDTOS = employeeConverter.convertToViewGetDTOList(employees);
        return viewGetEmployeeDTOS;
    }

    private List<String> getNameLegalsList(List<CompanyDTO> companyDTOList) {
        return companyDTOList.stream()
                .map(CompanyDTO::getNameLegal)
                .collect(Collectors.toList());
    }

    private Integer getStartIndex(Integer startIndex, Integer maxResult) {
        return (startIndex - 1) * maxResult;
    }
}
