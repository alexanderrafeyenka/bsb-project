package bsb.group5.employee.repository.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@EqualsAndHashCode
public class CompanyDTO {
    @JsonProperty("ID")
    private Long id;
    @JsonProperty("Name_Legal")
    private String nameLegal;
    @JsonProperty("UNP")
    private Integer unp;
    @JsonProperty("IBANbyBYN")
    private String ibanByByn;
    @JsonProperty("Type_Legal")
    private String typeLegal;
    @JsonProperty("Total_Employees")
    private Integer totalEmployees;
}
