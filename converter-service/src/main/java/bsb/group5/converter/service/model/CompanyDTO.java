package bsb.group5.converter.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class CompanyDTO {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "ID")
    private Long id;
    @JsonProperty("Name_Legal")
    private String nameLegal;
    @JsonProperty("UNP")
    private Integer unp;
    @JsonProperty("IBANbyBYN")
    private String ibanByByn;
    @JsonProperty("Total_Employees")
    private Integer totalEmployees;
}
