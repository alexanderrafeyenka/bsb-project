package bsb.group5.converter.service.model;

import bsb.group5.converter.service.model.enums.CurrencyDTOEnum;
import bsb.group5.converter.service.model.enums.StatusDTOEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.NumberFormat;

import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class ViewApplicationDTO {

    @JsonProperty("ApplicationConvId")
    private final UUID applicationId;
    @JsonProperty("Status")
    private final StatusDTOEnum status;
    @JsonProperty("Employee_Id")
    private final Long employeeId;
    @JsonProperty("Full_Name_Individual")
    private final String fullName;
    @JsonProperty("Percent_Conv")
    private final Float percentConversion;
    @JsonProperty("Value_Leg")
    private final CurrencyDTOEnum currencyConvertFrom;
    @JsonProperty("Value_Ind")
    private final CurrencyDTOEnum currencyConvertTo;
    @JsonProperty("Name_Legal")
    private final String nameLegal;
    @JsonProperty("Note")
    private final String note;
}
