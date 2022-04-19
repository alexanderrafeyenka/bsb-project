package bsb.group5.converter.service.converters;

import bsb.group5.converter.repository.model.Application;
import bsb.group5.converter.service.model.AddApplicationDTO;
import bsb.group5.converter.service.model.ViewApplicationDTO;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.List;

public interface ApplicationConverter {
    List<ViewApplicationDTO> convertToViewApplicationDTOList(Page<Application> applicationPages);

    List<ViewApplicationDTO> convertToViewApplicationDTOList(List<Application> applications);

    AddApplicationDTO convertCsvRecordToAddApplicationDTO(CSVRecord csvRecord) throws ParseException;

    ViewApplicationDTO convertToViewApplicationDTO(Application application);

    Application convertToApplication(AddApplicationDTO applicationDTO);
}
