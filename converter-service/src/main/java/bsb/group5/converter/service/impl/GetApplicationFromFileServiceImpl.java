package bsb.group5.converter.service.impl;

import bsb.group5.converter.service.GetApplicationFromFileService;
import bsb.group5.converter.service.converters.ApplicationConverter;
import bsb.group5.converter.service.model.AddApplicationDTO;
import bsb.group5.converter.service.validators.AddApplicationValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static bsb.group5.converter.service.model.enums.ResponseBodyFieldsEnum.InvalidEntryLines;

@Service
@Slf4j
@AllArgsConstructor
public class GetApplicationFromFileServiceImpl implements GetApplicationFromFileService {

    private final ApplicationConverter converter;
    private final AddApplicationValidator validator;

    @Override
    public List<AddApplicationDTO> get(Iterable<CSVRecord> csvRecords, Model responseBody) {
        List<AddApplicationDTO> applications = new ArrayList<>();
        List<Long> invalidEntries = new ArrayList<>();
        for (CSVRecord csvRecord : csvRecords) {
            boolean validRecord = validator.isValid(csvRecord);
            if (validRecord) {
                try {
                    AddApplicationDTO applicationDTO = converter.convertCsvRecordToAddApplicationDTO(csvRecord);
                    applications.add(applicationDTO);
                } catch (Exception e) {
                    log.warn(InvalidEntryLines.getMessage());
                    invalidEntries.add(csvRecord.getRecordNumber());
                }
            } else {
                log.warn(InvalidEntryLines.getMessage());
                invalidEntries.add(csvRecord.getRecordNumber());
            }
        }
        if (invalidEntries.size() > 0) {
            responseBody.addAttribute(InvalidEntryLines.getMessage(), invalidEntries);
        }
        return applications;
    }
}