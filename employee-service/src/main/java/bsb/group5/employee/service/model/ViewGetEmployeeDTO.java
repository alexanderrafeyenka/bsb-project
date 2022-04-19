package bsb.group5.employee.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ViewGetEmployeeDTO {
    @JsonProperty("Employee_Id")
    private Long employeeId;
    @JsonProperty("Full_Name_Individual")
    private String fullName;
    @JsonProperty("Recruitment_date")
    private String recruitmentDate;
    @JsonProperty("Termination_date")
    private String terminationDate;
    @JsonProperty("Name_Legal")
    private String nameLegal;
    @JsonProperty("Person_Iban_Byn")
    private String personIbanByn;
    @JsonProperty("Person_Iban_Currency")
    private String personIbanCurrency;
}