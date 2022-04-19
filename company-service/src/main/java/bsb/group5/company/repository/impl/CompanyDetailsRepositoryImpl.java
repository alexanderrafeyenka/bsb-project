package bsb.group5.company.repository.impl;

import bsb.group5.company.repository.CompanyDetailsRepository;
import bsb.group5.company.repository.model.CompanyDetails;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyDetailsRepositoryImpl extends GenericRepositoryImpl<Long, CompanyDetails> implements CompanyDetailsRepository {
}
