package bsb.group5.company.service.converters.impl;

import bsb.group5.company.repository.model.Company;
import bsb.group5.company.repository.model.SearchCompanyParameters;
import bsb.group5.company.service.converters.CompanyConverter;
import bsb.group5.company.service.model.AddCompanyDTO;
import bsb.group5.company.service.model.SearchCompanyDTO;
import bsb.group5.company.service.model.TypeLegalDTOEnum;
import bsb.group5.company.service.model.ViewCompanyDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class CompanyConverterImpl implements CompanyConverter {

    @Override
    public Company convert(AddCompanyDTO addCompanyDTO) {
        Company company = new Company();

        String nameLegal = addCompanyDTO.getNameLegal();
        company.setNameLegal(nameLegal);
        Integer unp = addCompanyDTO.getUnp();
        company.setUnp(String.valueOf(unp));
        String iban = addCompanyDTO.getIbanByByn();
        company.setIbanByByn(iban);
        boolean isResident = addCompanyDTO.isResident();
        company.setResident(isResident);
        Integer totalEmployees = addCompanyDTO.getTotalEmployees();
        company.setTotalEmployees(totalEmployees);
        return company;
    }

    @Override
    public ViewCompanyDTO convert(Company company) {
        Long id = company.getId();
        String nameLegal = company.getNameLegal();
        Integer unp = Integer.valueOf(company.getUnp());
        String ibanByByn = company.getIbanByByn();
        boolean isResident = company.isResident();
        TypeLegalDTOEnum typeOfLegal = getTypeOfLegal(isResident);
        Integer totalEmployees = company.getTotalEmployees();
        return ViewCompanyDTO.builder()
                .id(id)
                .nameLegal(nameLegal)
                .unp(unp)
                .ibanByByn(ibanByByn)
                .typeLegal(typeOfLegal)
                .totalEmployees(totalEmployees)
                .build();
    }

    public SearchCompanyParameters convert(SearchCompanyDTO searchCompanyDTO) {
        SearchCompanyParameters company = new SearchCompanyParameters();
        String nameLegal = searchCompanyDTO.getNameLegal();
        company.setNameLegal(nameLegal);
        Integer unp = searchCompanyDTO.getUnp();
        String stringOfUnp = getStringOfUnp(unp);
        company.setUnp(stringOfUnp);
        String iban = searchCompanyDTO.getIbanByByn();
        company.setIbanByByn(iban);
        return company;
    }

    private String getStringOfUnp(Integer unp) {
        if (unp == null) {
            return null;
        } else {
            return String.valueOf(unp);
        }
    }

    private TypeLegalDTOEnum getTypeOfLegal(boolean isResident) {
        if (isResident) {
            return TypeLegalDTOEnum.Resident;
        }
        return TypeLegalDTOEnum.NoResident;
    }
}
