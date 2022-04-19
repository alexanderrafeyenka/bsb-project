package bsb.group5.company.controllers;

import bsb.group5.company.config.MessageConfig;
import bsb.group5.company.controllers.model.PaginationEnum;
import bsb.group5.company.service.AddCompanyService;
import bsb.group5.company.service.GetCompanyService;
import bsb.group5.company.service.SearchCompanyService;
import bsb.group5.company.service.exceptions.CompanySearchException;
import bsb.group5.company.service.model.AddCompanyDTO;
import bsb.group5.company.service.model.PaginationDTO;
import bsb.group5.company.service.model.SearchCompanyDTO;
import bsb.group5.company.service.model.ViewCompanyDTO;
import bsb.group5.company.service.validators.CompanyUniqueValidator;
import bsb.group5.company.service.validators.CustomizeValid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("api/legals")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompanyController {
    private final AddCompanyService addCompanyService;
    private final GetCompanyService getCompanyService;
    private final SearchCompanyService searchCompanyService;
    private final CompanyUniqueValidator companyUniqueValidator;
    private final MessageConfig messageConfig;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> add(@RequestBody @Validated AddCompanyDTO addCompanyDTO) throws CompanySearchException {
        boolean isCompanyUnique = companyUniqueValidator.isValid(addCompanyDTO);
        if (isCompanyUnique) {
            ViewCompanyDTO addedCompany = addCompanyService.add(addCompanyDTO);
            Map<String, Object> resultBody = getResultBody(addedCompany);
            return new ResponseEntity<>(resultBody, CREATED);
        } else {
            String exceptionMessage = getFormattedExceptionMessage(addCompanyDTO);
            throw new CompanySearchException(exceptionMessage);
        }
    }

    @GetMapping(value = "/{LegalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ViewCompanyDTO> getById(@PathVariable(name = "LegalId") Long legalId) {
        ViewCompanyDTO viewCompanyDTO = getCompanyService.getById(legalId);
        return new ResponseEntity<>(viewCompanyDTO, OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ViewCompanyDTO>> search(
            @RequestParam(name = "Name_Legal", required = false)
            @NotBlank(message = "{message.validation.name.blank}")
            @Size(min = 3, message = "{message.validation.name.length}")
                    String nameLegal,
            @RequestParam(name = "UNP", required = false)
            @Min(value = 100, message = "{message.validation.unp.length}")
            @Positive(message = "{message.validation.unp.positive}")
                    Integer unp,
            @Length(min = 3, message = "{message.validation.iban.length}")
            @RequestParam(name = "IBANbyBYN", required = false)
                    String ibanByByn,
            @RequestParam(name = "page", required = false, defaultValue = "1")
                    Integer page,
            @RequestParam(name = "pagination", required = false, defaultValue = "NoPagination")
                    PaginationEnum pagination,
            @RequestParam(name = "customized_page", required = false)
            @CustomizeValid
                    Integer customizedPage
    ) throws CompanySearchException {
        PaginationDTO paginationDTO = PaginationDTO.builder()
                .pagination(pagination)
                .page(page)
                .customizedPage(customizedPage)
                .build();
        SearchCompanyDTO searchCompanyDTO = SearchCompanyDTO.builder()
                .ibanByByn(ibanByByn)
                .nameLegal(nameLegal)
                .unp(unp)
                .build();
        List<ViewCompanyDTO> listOfCompanies = searchCompanyService.search(searchCompanyDTO, paginationDTO);
        return new ResponseEntity<>(listOfCompanies, OK);
    }

    private Map<String, Object> getResultBody(ViewCompanyDTO addedCompany) {
        Map<String, Object> resultBody = new LinkedHashMap<>();
        resultBody.put("timestamp", String.valueOf(LocalDateTime.now()));
        resultBody.put("status", CREATED);
        resultBody.put("message", messageConfig.getMessageCompanyAdded());
        resultBody.put("body", addedCompany);
        return resultBody;
    }

    private String getFormattedExceptionMessage(AddCompanyDTO addCompanyDTO) {
        String ibanByByn = addCompanyDTO.getIbanByByn();
        String nameLegal = addCompanyDTO.getNameLegal();
        Integer unp = addCompanyDTO.getUnp();
        return String.format("%s %s %s %s", messageConfig.getMessageCompanyExists(), nameLegal, unp, ibanByByn);
    }
}