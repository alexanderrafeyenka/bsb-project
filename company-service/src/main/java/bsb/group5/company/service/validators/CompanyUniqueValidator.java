package bsb.group5.company.service.validators;

import bsb.group5.company.repository.CompanyRepository;
import bsb.group5.company.repository.model.Company;
import bsb.group5.company.service.model.AddCompanyDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class CompanyUniqueValidator {

    private final CompanyRepository companyRepository;

    public boolean isValid(AddCompanyDTO companyDTO) {
        String name = companyDTO.getNameLegal();
        Optional<Company> companyByName = companyRepository.findByName(name);
        Integer unp = companyDTO.getUnp();
        String unpAsString = String.valueOf(unp);
        Optional<Company> companyByUnp = companyRepository.findByUnp(unpAsString);
        String iban = companyDTO.getIbanByByn();
        Optional<Company> companyByIban = companyRepository.findByIban(iban);
        return companyByName.isEmpty() && companyByIban.isEmpty() && companyByUnp.isEmpty();
    }
}
