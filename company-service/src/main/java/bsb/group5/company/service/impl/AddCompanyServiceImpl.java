package bsb.group5.company.service.impl;

import bsb.group5.company.repository.CompanyDetailsRepository;
import bsb.group5.company.repository.CompanyRepository;
import bsb.group5.company.repository.model.Company;
import bsb.group5.company.repository.model.CompanyDetails;
import bsb.group5.company.service.AddCompanyService;
import bsb.group5.company.service.converters.CompanyConverter;
import bsb.group5.company.service.model.AddCompanyDTO;
import bsb.group5.company.service.model.ViewCompanyDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@AllArgsConstructor
public class AddCompanyServiceImpl implements AddCompanyService {

    private final CompanyConverter companyConverter;
    private final CompanyRepository companyRepository;
    private CompanyDetailsRepository companyDetailsRepository;

    @Override
    @Transactional
    public ViewCompanyDTO add(AddCompanyDTO addCompanyDTO) {
        Company company = companyConverter.convert(addCompanyDTO);
        Company savedCompany = companyRepository.persist(company);
        CompanyDetails companyDetails = getCompanyDetails(savedCompany);
        companyDetailsRepository.persist(companyDetails);
        return companyConverter.convert(savedCompany);
    }

    private CompanyDetails getCompanyDetails(Company company) {
        CompanyDetails companyDetails = new CompanyDetails();
        companyDetails.setCompany(company);
        companyDetails.setCreateDate(LocalDateTime.now());
        companyDetails.setLastUpdate(LocalDateTime.now());
        return companyDetails;
    }
}
