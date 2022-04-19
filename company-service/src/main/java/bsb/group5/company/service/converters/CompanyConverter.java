package bsb.group5.company.service.converters;

import bsb.group5.company.repository.model.Company;
import bsb.group5.company.repository.model.SearchCompanyParameters;
import bsb.group5.company.service.model.AddCompanyDTO;
import bsb.group5.company.service.model.SearchCompanyDTO;
import bsb.group5.company.service.model.ViewCompanyDTO;

public interface CompanyConverter {
    Company convert(AddCompanyDTO addCompanyDTO);

    ViewCompanyDTO convert(Company company);

    SearchCompanyParameters convert(SearchCompanyDTO searchCompanyDTO);
}
