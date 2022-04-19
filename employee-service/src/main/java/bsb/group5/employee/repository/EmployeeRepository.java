package bsb.group5.employee.repository;

import bsb.group5.employee.repository.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends GenericRepository<Long, Employee> {
    Optional<Employee> findByName(String fullName);

    Optional<Employee> findByIbanByn(String ibanByn);

    Optional<Employee> findByIbanCurrency(String ibanCurrency);

    List<Employee> findByNameLegals(List<String> nameLegals, String fullName, Integer firstIndex, Integer maxResult);
}
