package bsb.group5.company.service;

import bsb.group5.company.service.exceptions.CompanySearchException;
import bsb.group5.company.service.model.PaginationDTO;
import bsb.group5.company.service.model.SearchCompanyDTO;
import bsb.group5.company.service.model.ViewCompanyDTO;

import java.util.List;

public interface SearchCompanyService {
    List<ViewCompanyDTO> search(SearchCompanyDTO searchCompanyDTO, PaginationDTO paginationDTO) throws CompanySearchException;
}
