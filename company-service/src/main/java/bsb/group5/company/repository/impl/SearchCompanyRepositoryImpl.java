package bsb.group5.company.repository.impl;

import bsb.group5.company.repository.SearchCompanyRepository;
import bsb.group5.company.repository.model.Company;
import bsb.group5.company.repository.model.SearchCompanyParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class SearchCompanyRepositoryImpl extends GenericRepositoryImpl<String, Company> implements SearchCompanyRepository {

    @Override
    public List<Company> search(SearchCompanyParameters searchModel, Integer startIndex, Integer maxResult) {
        String name = searchModel.getNameLegal();
        String unp = searchModel.getUnp();
        String iban = searchModel.getIbanByByn();

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Company> criteriaQuery = criteriaBuilder.createQuery(Company.class);
        Root<Company> companyRoot = criteriaQuery.from(Company.class);

        List<Predicate> predicates = new ArrayList<>();
        if (name != null) {
            predicates.add(criteriaBuilder.like(companyRoot.get("nameLegal"), name + '%'));
        }
        if (unp != null) {
            predicates.add(criteriaBuilder.like(companyRoot.get("unp"), unp + '%'));
        }
        if (iban != null) {
            predicates.add(criteriaBuilder.like(companyRoot.get("ibanByByn"), iban + '%'));
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        if (startIndex != null && maxResult != null) {
            TypedQuery<Company> typedQuery = em.createQuery(criteriaQuery);
            typedQuery.setFirstResult(startIndex);
            typedQuery.setMaxResults(maxResult);
            return typedQuery.getResultList();
        } else {
            return em.createQuery(criteriaQuery).getResultList();
        }
    }
}
