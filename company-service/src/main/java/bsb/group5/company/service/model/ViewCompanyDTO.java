package bsb.group5.company.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@ToString
@Getter
@Setter
@Builder(toBuilder = true)
public class ViewCompanyDTO {
    @JsonProperty("ID")
    private Long id;
    @Length(max = 255)
    @JsonProperty("Name_Legal")
    private String nameLegal;
    @JsonProperty("UNP")
    private Integer unp;
    @JsonProperty("IBANbyBYN")
    private String ibanByByn;
    @JsonProperty("Type_Legal")
    private TypeLegalDTOEnum typeLegal;
    @JsonProperty("Total_Employees")
    private Integer totalEmployees;
}
