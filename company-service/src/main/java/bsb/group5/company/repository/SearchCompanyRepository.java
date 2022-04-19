package bsb.group5.company.repository;

import bsb.group5.company.repository.model.Company;
import bsb.group5.company.repository.model.SearchCompanyParameters;

import java.util.List;

public interface SearchCompanyRepository extends GenericRepository<String, Company> {
    List<Company> search(SearchCompanyParameters searchCompanyParameters, Integer startIndex, Integer maxResult);
}
