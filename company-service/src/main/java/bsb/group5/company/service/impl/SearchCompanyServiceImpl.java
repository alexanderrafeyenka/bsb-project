package bsb.group5.company.service.impl;

import bsb.group5.company.config.MessageConfig;
import bsb.group5.company.config.PaginationConfig;
import bsb.group5.company.controllers.model.PaginationEnum;
import bsb.group5.company.repository.SearchCompanyRepository;
import bsb.group5.company.repository.model.Company;
import bsb.group5.company.repository.model.SearchCompanyParameters;
import bsb.group5.company.service.SearchCompanyService;
import bsb.group5.company.service.converters.CompanyConverter;
import bsb.group5.company.service.exceptions.CompanySearchException;
import bsb.group5.company.service.model.PaginationDTO;
import bsb.group5.company.service.model.SearchCompanyDTO;
import bsb.group5.company.service.model.ViewCompanyDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SearchCompanyServiceImpl implements SearchCompanyService {

    private final SearchCompanyRepository searchCompanyRepository;
    private final CompanyConverter companyConverter;
    private final PaginationConfig paginationConfig;
    private final MessageConfig messageConfig;

    @Override
    @Transactional
    public List<ViewCompanyDTO> search(SearchCompanyDTO searchCompanyDTO, PaginationDTO paginationDTO) throws CompanySearchException {
        PaginationEnum pagination = paginationDTO.getPagination();
        SearchCompanyParameters parametersToSearch = companyConverter.convert(searchCompanyDTO);
        List<Company> companies;
        switch (pagination) {
            case Default:
                int maxResult = Integer.parseInt(paginationConfig.getPaginationMaxDefault());
                Integer startIndexDefault = getStartIndex(paginationDTO.getPage(), maxResult);
                companies = searchCompanyRepository.search(parametersToSearch, startIndexDefault, maxResult);
                break;
            case Customized:
                Integer startIndexCustomized = getStartIndex(paginationDTO.getPage(), paginationDTO.getCustomizedPage());
                companies = searchCompanyRepository.search(parametersToSearch, startIndexCustomized, paginationDTO.getCustomizedPage());
                break;
            default:
                companies = searchCompanyRepository.search(parametersToSearch, null, null);
                break;
        }
        if (companies.isEmpty()) {
            throw new CompanySearchException(messageConfig.getMessageCompanyNotFound());
        }
        return companies.stream()
                .map(companyConverter::convert)
                .collect(Collectors.toList());
    }

    private Integer getStartIndex(Integer startIndex, Integer maxResult) {
        return (startIndex - 1) * maxResult;
    }
}
