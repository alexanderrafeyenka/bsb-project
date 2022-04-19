package bsb.group5.employee.repository.impl;

import bsb.group5.employee.repository.EmployeeDetailsRepository;
import bsb.group5.employee.repository.model.EmployeeDetails;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeDetailsRepositoryImpl extends GenericRepositoryImpl<Long, EmployeeDetails> implements EmployeeDetailsRepository {
}
