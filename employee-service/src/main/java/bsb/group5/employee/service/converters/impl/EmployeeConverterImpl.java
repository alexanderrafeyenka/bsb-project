package bsb.group5.employee.service.converters.impl;

import bsb.group5.employee.repository.model.Employee;
import bsb.group5.employee.service.converters.EmployeeConverter;
import bsb.group5.employee.service.model.AddEmployeeDTO;
import bsb.group5.employee.service.model.ViewAddEmployeeDTO;
import bsb.group5.employee.service.model.ViewGetEmployeeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmployeeConverterImpl implements EmployeeConverter {

    @Override
    public Employee convert(AddEmployeeDTO addEmployeeDTO) {
        Employee employee = new Employee();
        employee.setFullName(addEmployeeDTO.getFullName());
        employee.setNameLegal(addEmployeeDTO.getNameLegal());
        employee.setPersonIbanByn(addEmployeeDTO.getPersonIbanByn());
        employee.setPersonIbanCurrency(addEmployeeDTO.getPersonIbanCurrency());
        return employee;
    }

    @Override
    public ViewAddEmployeeDTO convertToViewAddDTO(Employee employee) {
        ViewAddEmployeeDTO viewAddEmployeeDTO = new ViewAddEmployeeDTO();
        Long id = employee.getId();
        viewAddEmployeeDTO.setEmployeeId(id);
        return viewAddEmployeeDTO;
    }

    @Override
    public ViewGetEmployeeDTO convertToViewGetDTO(Employee employee) {
        ViewGetEmployeeDTO viewGetEmployeeDTO = new ViewGetEmployeeDTO();

        Long id = employee.getId();
        viewGetEmployeeDTO.setEmployeeId(id);

        String fullName = employee.getFullName();
        viewGetEmployeeDTO.setFullName(fullName);

        String nameLegal = employee.getNameLegal();
        viewGetEmployeeDTO.setNameLegal(nameLegal);

        LocalDate recruitmentDate = employee.getRecruitmentDate();
        String formattedRecruitmentDate = getFormattedDate(recruitmentDate);
        viewGetEmployeeDTO.setRecruitmentDate(formattedRecruitmentDate);

        LocalDate terminationDate = employee.getTerminationDate();
        String formattedTerminationDate = getFormattedDate(terminationDate);
        viewGetEmployeeDTO.setTerminationDate(formattedTerminationDate);

        String personIbanByn = employee.getPersonIbanByn();
        viewGetEmployeeDTO.setPersonIbanByn(personIbanByn);

        String personIbanCurrency = employee.getPersonIbanCurrency();
        viewGetEmployeeDTO.setPersonIbanCurrency(personIbanCurrency);
        return viewGetEmployeeDTO;
    }

    private String getFormattedDate(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return localDate.format(formatter);
    }

    @Override
    public List<ViewGetEmployeeDTO> convertToViewGetDTOList(List<Employee> employees) {
        return employees.stream()
                .map(this::convertToViewGetDTO)
                .collect(Collectors.toList());
    }
}
