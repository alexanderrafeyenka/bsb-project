package bsb.group5.converter.service.model;

import bsb.group5.converter.service.model.enums.CurrencyDTOEnum;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class AddApplicationDTO {

    private final UUID applicationId;
    private final CurrencyDTOEnum currencyConvertFrom;
    private final CurrencyDTOEnum currencyConvertTo;
    private final Float percentConverted;
    private final Long employeeId;
    private final String nameLegal;
    private final String note;
}

