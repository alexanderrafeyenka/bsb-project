package bsb.group5.converter.service.converters.impl;

import bsb.group5.converter.repository.EmployeeFeignRepository;
import bsb.group5.converter.repository.model.Application;
import bsb.group5.converter.repository.model.ApplicationDetails;
import bsb.group5.converter.repository.model.CurrencyEnum;
import bsb.group5.converter.repository.model.StatusEnum;
import bsb.group5.converter.service.converters.ApplicationConverter;
import bsb.group5.converter.service.model.AddApplicationDTO;
import bsb.group5.converter.service.model.enums.CurrencyDTOEnum;
import bsb.group5.converter.service.model.EmployeeDTO;
import bsb.group5.converter.service.model.enums.StatusDTOEnum;
import bsb.group5.converter.service.model.ViewApplicationDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static bsb.group5.converter.service.model.enums.AddApplicationColumnHeadersEnum.*;

@Slf4j
@Component
@AllArgsConstructor
public class ApplicationConverterImpl implements ApplicationConverter {

    private final EmployeeFeignRepository employeeFeignRepository;

    @Override
    public List<ViewApplicationDTO> convertToViewApplicationDTOList(Page<Application> applicationPages) {
        return applicationPages.stream()
                .map(this::convertToViewApplicationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ViewApplicationDTO> convertToViewApplicationDTOList(List<Application> applications) {
        return applications.stream()
                .map(this::convertToViewApplicationDTO)
                .collect(Collectors.toList());
    }

    @Override

    public AddApplicationDTO convertCsvRecordToAddApplicationDTO(CSVRecord csvRecord) {
        UUID uuid = UUID.fromString(csvRecord.get(ApplicationConvId));
        CurrencyDTOEnum currencyConvertedFrom = CurrencyDTOEnum.valueOf(csvRecord.get(Value_Leg));
        CurrencyDTOEnum currencyConvertedTo = CurrencyDTOEnum.valueOf(csvRecord.get(Value_Ind));
        String sourceValue = csvRecord.get(Percent_Conv);
        sourceValue = sourceValue.replace(',', '.');
        Float percentConverted = Float.parseFloat(sourceValue);
        Long employeeId = Long.valueOf(csvRecord.get(EmployeeId));
        String nameLegal = csvRecord.get(NameLegal);
        String note = csvRecord.get(Note);
        return AddApplicationDTO.builder()
                .applicationId(uuid)
                .currencyConvertFrom(currencyConvertedFrom)
                .currencyConvertTo(currencyConvertedTo)
                .percentConverted(percentConverted)
                .employeeId(employeeId)
                .nameLegal(nameLegal)
                .note(note)
                .build();
    }

    @Override
    public ViewApplicationDTO convertToViewApplicationDTO(Application application) {
        UUID uuid = application.getUuid();
        StatusEnum status = application.getStatus();
        StatusDTOEnum statusDTOEnum = StatusDTOEnum.valueOf(status.toString());
        Long employeeId = application.getEmployeeId();
        Float percentConverted = application.getPercentConv();
        CurrencyEnum currencyConvertFrom = application.getCurrencyConvertFrom();
        CurrencyDTOEnum currencyDTOFromEnum = CurrencyDTOEnum.valueOf(currencyConvertFrom.toString());
        CurrencyEnum currencyConvertTo = application.getCurrencyConvertTo();
        CurrencyDTOEnum currencyDTOToEnum = CurrencyDTOEnum.valueOf(currencyConvertTo.toString());
        String nameLegal = application.getNameLegal();
        EmployeeDTO employeeDTO = employeeFeignRepository.getEmployeeById(employeeId);
        String fullName = employeeDTO.getFullName();
        return ViewApplicationDTO.builder()
                .applicationId(uuid)
                .status(statusDTOEnum)
                .employeeId(employeeId)
                .percentConversion(percentConverted)
                .currencyConvertFrom(currencyDTOFromEnum)
                .currencyConvertTo(currencyDTOToEnum)
                .nameLegal(nameLegal)
                .fullName(fullName)
                .build();
    }

    @Override
    public Application convertToApplication(AddApplicationDTO applicationDTO) {
        Application application = new Application();
        application.setUuid(applicationDTO.getApplicationId());
        application.setCurrencyConvertFrom(CurrencyEnum.valueOf(applicationDTO.getCurrencyConvertFrom().name()));
        application.setCurrencyConvertTo(CurrencyEnum.valueOf(applicationDTO.getCurrencyConvertTo().name()));
        application.setEmployeeId(applicationDTO.getEmployeeId());
        application.setNameLegal(applicationDTO.getNameLegal());
        application.setPercentConv(applicationDTO.getPercentConverted());
        application.setStatus(StatusEnum.New);
        return application;
    }
}