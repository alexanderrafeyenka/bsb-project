package bsb.group5.converter.service.validators;

import org.apache.commons.csv.CSVRecord;

public interface AddApplicationValidator {
    boolean isValid(CSVRecord csvRecord);
}
