package bsb.group5.company.service.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@EqualsAndHashCode
public class SearchCompanyDTO {
    private String nameLegal;
    private Integer unp;
    private String ibanByByn;
}
