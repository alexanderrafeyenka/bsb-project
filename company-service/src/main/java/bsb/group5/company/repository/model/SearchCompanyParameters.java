package bsb.group5.company.repository.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class SearchCompanyParameters {

    private String nameLegal;
    private String unp;
    private String ibanByByn;
}
