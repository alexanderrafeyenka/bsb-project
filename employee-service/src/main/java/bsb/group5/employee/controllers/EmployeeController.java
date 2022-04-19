package bsb.group5.employee.controllers;

import bsb.group5.employee.config.MessageConfig;
import bsb.group5.employee.service.AddEmployeeService;
import bsb.group5.employee.service.GetAllEmployeeService;
import bsb.group5.employee.service.GetEmployeeService;
import bsb.group5.employee.service.SearchEmployeeService;
import bsb.group5.employee.service.exceptions.EmployeeSearchException;
import bsb.group5.employee.service.model.AddEmployeeDTO;
import bsb.group5.employee.service.model.EmployeeSearchDTO;
import bsb.group5.employee.service.model.PaginationDTO;
import bsb.group5.employee.service.model.PaginationEnum;
import bsb.group5.employee.service.model.ViewAddEmployeeDTO;
import bsb.group5.employee.service.model.ViewGetEmployeeDTO;
import bsb.group5.employee.service.validators.CustomizeValid;
import bsb.group5.employee.service.validators.EmployeeExistsValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "api/employees")
@RestController
@AllArgsConstructor
@Slf4j
@Validated
public class EmployeeController {

    private final EmployeeExistsValidator existsValidator;
    private final AddEmployeeService addEmployeeService;
    private final MessageConfig messageConfig;
    private final GetEmployeeService getEmployeeService;
    private final GetAllEmployeeService getallEmployeeService;
    private final SearchEmployeeService searchEmployeeService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> add(@RequestBody @Validated AddEmployeeDTO addEmployeeDTO) {
        boolean isEmployeeAbsent = existsValidator.isValid(addEmployeeDTO);
        if (isEmployeeAbsent) {
            ViewAddEmployeeDTO addedCompany = addEmployeeService.add(addEmployeeDTO);
            Map<String, Object> resultBody = getResultBodyAddEmployee(addedCompany);
            return new ResponseEntity<>(resultBody, HttpStatus.CREATED);
        } else {
            throw new EmployeeSearchException(messageConfig.getMessageEmployeeExists());
        }
    }

    @GetMapping(value = "/{Employee_Id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ViewGetEmployeeDTO> getById(@PathVariable(name = "Employee_Id") Long employeeId) {
        ViewGetEmployeeDTO viewGetEmployeeDTO = getEmployeeService.getById(employeeId);
        log.info(viewGetEmployeeDTO.toString());
        return new ResponseEntity<>(viewGetEmployeeDTO, HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ViewGetEmployeeDTO>> getEmployee(
            @RequestParam(name = "Name_Legal", required = false)
            @Size(min = 3, message = "{message.validation.name.length}")
                    String nameLegal,
            @RequestParam(name = "UNP", required = false)
            @Min(value = 100, message = "{message.validation.unp.length}")
            @Positive(message = "{message.validation.unp.positive}")
                    Integer unp,
            @Length(min = 3, max = 255, message = "${message.invalid.parameters}")
            @Pattern(regexp = "([А-ЯЁа-яё ]){3,}", message = "{message.invalid.parameters}")
            @RequestParam(name = "Full_Name_Individual", required = false)
                    String fullName,
            @RequestParam(name = "page", required = false, defaultValue = "1")
                    Integer page,
            @RequestParam(name = "pagination", required = false, defaultValue = "NoPagination")
                    PaginationEnum pagination,
            @RequestParam(name = "customized_page", required = false)
            @CustomizeValid
                    Integer customizedPage
    ) {
        EmployeeSearchDTO companySearchDTO = EmployeeSearchDTO.builder()
                .fullName(fullName)
                .nameLegal(nameLegal)
                .unp(unp)
                .build();
        PaginationDTO paginationDTO = PaginationDTO.builder()
                .pagination(pagination)
                .page(page)
                .customizedPage(customizedPage)
                .build();
        if (companySearchDTO.getFullName() != null || companySearchDTO.getUnp() != null || companySearchDTO.getNameLegal() != null) {
            List<ViewGetEmployeeDTO> viewGetEmployeeDTOS = searchEmployeeService.search(companySearchDTO, paginationDTO);
            return new ResponseEntity<>(viewGetEmployeeDTOS, HttpStatus.OK);
        } else {
            List<ViewGetEmployeeDTO> viewGetEmployeeDTOS = getallEmployeeService.getAll(paginationDTO);
            return new ResponseEntity<>(viewGetEmployeeDTOS, HttpStatus.OK);
        }
    }

    private Map<String, Object> getResultBodyAddEmployee(ViewAddEmployeeDTO viewAddEmployeeDTO) {
        Map<String, Object> resultBody = new LinkedHashMap<>();
        resultBody.put(messageConfig.getMessageFieldTimestamp(), String.valueOf(LocalDateTime.now()));
        resultBody.put(messageConfig.getMessageFieldStatus(), HttpStatus.CREATED);
        resultBody.put(messageConfig.getMessageFieldMessage(), messageConfig.getMessageEmployeeAdded());
        resultBody.put(messageConfig.getMessageFieldBody(), viewAddEmployeeDTO);
        return resultBody;
    }
}
