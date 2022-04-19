package bsb.group5.converter.service;

import bsb.group5.converter.service.model.AddApplicationDTO;
import org.apache.commons.csv.CSVRecord;
import org.springframework.ui.Model;

import java.util.List;

public interface GetApplicationFromFileService {
    List<AddApplicationDTO> get(Iterable<CSVRecord> csvRecords, Model responseBody);
}
