package bsb.group5.company.repository.impl;

import bsb.group5.company.repository.CompanyRepository;
import bsb.group5.company.repository.model.Company;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class CompanyRepositoryImpl extends GenericRepositoryImpl<Long, Company> implements CompanyRepository {
    @Override
    public Optional<Company> findByUnp(String unp) {
        String command = "SELECT c FROM Company AS c WHERE c.unp=:unp";
        Query query = em.createQuery(command);
        query.setParameter("unp", unp);
        try {
            Company company = (Company) query.getSingleResult();
            return Optional.of(company);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Company> findByName(String name) {
        String command = "SELECT c FROM Company AS c WHERE c.nameLegal=:name";
        Query query = em.createQuery(command);
        query.setParameter("name", name);
        try {
            Company company = (Company) query.getSingleResult();
            return Optional.of(company);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Company> findByIban(String iban) {
        String command = "SELECT c FROM Company AS c WHERE c.ibanByByn=:iban";
        Query query = em.createQuery(command);
        query.setParameter("iban", iban);
        try {
            Company company = (Company) query.getSingleResult();
            return Optional.of(company);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
