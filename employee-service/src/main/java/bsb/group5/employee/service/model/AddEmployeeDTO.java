package bsb.group5.employee.service.model;

import bsb.group5.employee.service.validators.CompanyActual;
import bsb.group5.employee.service.validators.EmployeeDates;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EmployeeDates
@EqualsAndHashCode
public class AddEmployeeDTO {

    @JsonProperty("Full_Name_Individual")
    @NotNull(message = "{message.invalid.parameters}")
    @Size(max = 255, message = "${message.invalid.parameters}")
    @Pattern(regexp = "([А-ЯЁ][а-яё]+[\\-\\s]?){3,}", message = "{message.invalid.parameters}")
    private String fullName;

    @JsonProperty("Recruitment_date")
    @NotNull(message = "{message.invalid.parameters}")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate recruitmentDate;

    @JsonProperty("Termination_date")
    @NotNull(message = "{message.invalid.parameters}")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate terminationDate;

    @JsonProperty("Name_Legal")
    @NotNull(message = "{message.invalid.parameters}")
    @Size(message = "{message.invalid.parameters}", max = 255)
    @CompanyActual
    private String nameLegal;

    @JsonProperty("Person_Iban_Byn")
    @Size(min = 28, max = 28, message = "{message.invalid.parameters}")
    @Pattern(regexp = "BY[0-9]{2}UNBS[0-9]{7}([a-zA-Z0-9]?){16}", message = "{message.invalid.parameters}")
    private String personIbanByn;

    @JsonProperty("Person_Iban_Currency")
    @Size(min = 28, max = 28, message = "{message.invalid.parameters}")
    @Pattern(regexp = "BY[0-9]{2}UNBS[0-9]{7}([a-zA-Z0-9]?){16}", message = "{message.invalid.parameters}")
    private String personIbanCurrency;
}
