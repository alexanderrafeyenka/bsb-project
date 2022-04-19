package bsb.group5.company.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class AddCompanyDTO {
    @NotNull(message = "{message.invalid.parameters}")
    @Length(message = "{message.invalid.parameters}", max = 255)
    @JsonProperty("Name_Legal")
    private String nameLegal;
    @NotNull(message = "{message.invalid.parameters}")
    @Positive(message = "{message.invalid.parameters}")
    @Max(value = 999999999, message = "{message.invalid.parameters}")
    @Min(value = 100000000, message = "{message.invalid.parameters}")
    @JsonProperty("UNP")
    private Integer unp;
    @NotNull(message = "{message.invalid.parameters}")
    @Length(min = 28, max = 28, message = "{message.invalid.parameters}")
    @Pattern(regexp = "BY[0-9]{2}UNBS[0-9]{7}([a-zA-Z0-9]?){16}", message = "{message.invalid.parameters}")
    @JsonProperty("IBANbyBYN")
    private String ibanByByn;
    @NotNull(message = "{message.invalid.parameters}")
    @JsonProperty("Type_Legal")
    private boolean isResident;
    @NotNull(message = "{message.invalid.parameters}")
    @JsonProperty("Total_Employees")
    private Integer totalEmployees;
}
