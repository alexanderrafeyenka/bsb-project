package bsb.group5.company.service.impl;

import bsb.group5.company.config.MessageConfig;
import bsb.group5.company.repository.CompanyRepository;
import bsb.group5.company.repository.model.Company;
import bsb.group5.company.service.GetCompanyService;
import bsb.group5.company.service.converters.CompanyConverter;
import bsb.group5.company.service.exceptions.CompanyViewException;
import bsb.group5.company.service.model.ViewCompanyDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class GetCompanyServiceImpl implements GetCompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyConverter companyConverter;
    private final MessageConfig messageConfig;

    @Override
    @Transactional
    public ViewCompanyDTO getById(Long id) {
        Company company = companyRepository.findById(id);
        if (company == null) {
            throw new CompanyViewException(messageConfig.getMessageCompanyNotExists());
        }
        return companyConverter.convert(company);
    }
}
