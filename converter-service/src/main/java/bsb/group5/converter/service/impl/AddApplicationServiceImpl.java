package bsb.group5.converter.service.impl;

import bsb.group5.converter.service.AddApplicationService;
import bsb.group5.converter.service.GetApplicationFromFileService;
import bsb.group5.converter.service.ParseCsvFileService;
import bsb.group5.converter.service.SaveApplicationService;
import bsb.group5.converter.service.exceptions.ParseCSVException;
import bsb.group5.converter.service.model.AddApplicationDTO;
import bsb.group5.converter.service.model.enums.ResponseBodyFieldsEnum;
import bsb.group5.converter.service.model.ViewApplicationDTO;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static bsb.group5.converter.service.model.enums.ResponseBodyFieldsEnum.*;

@Service
@AllArgsConstructor
public class AddApplicationServiceImpl implements AddApplicationService {

    private final ParseCsvFileService parseCsvFileService;
    private final GetApplicationFromFileService getApplicationFromFileService;
    private final SaveApplicationService saveApplicationService;

    @Override
    public ResponseEntity<Model> addByFile(MultipartFile dtoFile, Model responseBody) {
        Iterable<CSVRecord> csvRecords;
        try {
            csvRecords = parseCsvFileService.parse(dtoFile, responseBody);
        } catch (ParseCSVException e) {
            responseBody.addAttribute(ResponseBodyFieldsEnum.InvalidEntryLines.getMessage(), e.getMessage());
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }

        List<AddApplicationDTO> applicationDTOList = getApplicationFromFileService.get(csvRecords, responseBody);
        List<Long> invalidEntries = (List<Long>) responseBody.getAttribute(InvalidEntryLines.getMessage());
        if (invalidEntries != null) {
            responseBody.addAttribute(ResponseBodyFieldsEnum.InvalidEntryLines.getMessage(), invalidEntries);
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }

        Set<AddApplicationDTO> nonRepeatedApplications = new HashSet<>(applicationDTOList);
        if (nonRepeatedApplications.size() != applicationDTOList.size()) {
            Set<UUID> repeatedApplicationIds = applicationDTOList.stream()
                    .filter(element -> Collections.frequency(applicationDTOList, element) > 1)
                    .distinct()
                    .map(AddApplicationDTO::getApplicationId)
                    .collect(Collectors.toSet());
            responseBody.addAttribute(RepeatedApplications.getMessage(), repeatedApplicationIds);
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }

        Set<AddApplicationDTO> existingApplications = saveApplicationService.filterExisting(nonRepeatedApplications);
        if (existingApplications.size() > 0) {
            Set<UUID> existingUuids = existingApplications.stream()
                    .map(AddApplicationDTO::getApplicationId)
                    .collect(Collectors.toSet());
            responseBody.addAttribute(DuplicateApplications.getMessage(), existingUuids);
            return new ResponseEntity<>(responseBody, HttpStatus.CONFLICT);
        } else {
            Set<ViewApplicationDTO> savedApplications = saveApplicationService.saveAll(nonRepeatedApplications);
            responseBody.addAttribute(SavedApplications.getMessage(), savedApplications);
            return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
        }
    }
}
