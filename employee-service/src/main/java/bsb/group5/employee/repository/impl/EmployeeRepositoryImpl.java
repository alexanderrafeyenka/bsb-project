package bsb.group5.employee.repository.impl;

import bsb.group5.employee.repository.EmployeeRepository;
import bsb.group5.employee.repository.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class EmployeeRepositoryImpl extends GenericRepositoryImpl<Long, Employee> implements EmployeeRepository {
    @Override
    public Optional<Employee> findByName(String fullName) {
        String command = "SELECT e FROM Employee AS e WHERE e.fullName=:name";
        Query query = em.createQuery(command);
        query.setParameter("name", fullName);
        try {
            Employee employee = (Employee) query.getSingleResult();
            return Optional.of(employee);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Employee> findByIbanByn(String ibanByn) {
        String command = "SELECT e FROM Employee AS e WHERE e.personIbanByn=:ibanByn";
        Query query = em.createQuery(command);
        query.setParameter("ibanByn", ibanByn);
        try {
            Employee employee = (Employee) query.getSingleResult();
            return Optional.of(employee);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Employee> findByIbanCurrency(String ibanCurrency) {
        String command = "SELECT e FROM Employee AS e WHERE e.personIbanCurrency=:ibanCurrency";
        Query query = em.createQuery(command);
        query.setParameter("ibanCurrency", ibanCurrency);
        try {
            Employee employee = (Employee) query.getSingleResult();
            return Optional.of(employee);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Employee> findByNameLegals(List<String> nameLegals, String fullName, Integer firstIndex, Integer maxResult) {
        String command = "SELECT e FROM Employee AS e WHERE e.nameLegal IN :nameLegals";
        if (fullName != null) {
            command = command + " AND e.fullName LIKE '" + fullName + "%'";
        }
        log.info(command);
        Query query = em.createQuery(command);
        query.setParameter("nameLegals", nameLegals);
        if (firstIndex != null && maxResult != null) {
            query.setFirstResult(firstIndex);
            query.setMaxResults(maxResult);
        }
        return query.getResultList();
    }
}
