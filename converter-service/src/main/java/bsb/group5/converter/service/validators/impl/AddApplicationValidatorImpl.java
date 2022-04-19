package bsb.group5.converter.service.validators.impl;

import bsb.group5.converter.service.validators.AddApplicationValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;

import static bsb.group5.converter.service.model.enums.AddApplicationColumnHeadersEnum.*;

@Component
@Validated
@Slf4j
public class AddApplicationValidatorImpl implements AddApplicationValidator {

    private static final String UUID_PATTERN = "[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}";

    @Override
    public boolean isValid(CSVRecord csvRecord) {
        try {
            String percentAsString = csvRecord.get(Percent_Conv);
            float percentConverted = getPercentAsStringConvertedToFloatOfProperFormat(percentAsString);
            long employeeId = Long.parseLong(csvRecord.get(EmployeeId));
            String uuidAsString = csvRecord.get(ApplicationConvId);
            boolean matchesPattern = uuidAsString.matches(UUID_PATTERN);
            boolean positiveId = employeeId > 0;
            boolean isPercentValue = percentConverted <= 100 && percentConverted >= 0;
            return matchesPattern && positiveId && isPercentValue;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return false;
        }
    }

    private float getPercentAsStringConvertedToFloatOfProperFormat(String percentAsString) throws ParseException {
        String percentFormatted = percentAsString.replace(',', '.');
        DecimalFormat format = new DecimalFormat("0.####");
        format.setRoundingMode(RoundingMode.DOWN);
        return format.parse(percentFormatted).floatValue();
    }
}
