package bsb.group5.company.repository;

import bsb.group5.company.repository.model.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends GenericRepository<Long, Company> {
    Optional<Company> findByUnp(String unp);

    Optional<Company> findByName(String name);

    Optional<Company> findByIban(String iban);
}
