package bsb.group5.auth.repository.impl;

import bsb.group5.auth.repository.UserRoleRepository;
import bsb.group5.auth.repository.model.RoleEnum;
import bsb.group5.auth.repository.model.UserRole;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Optional;

@Repository
public class UserRoleRepositoryImpl extends GenericRepositoryImpl<Long, UserRole> implements UserRoleRepository {
    @Override
    public Optional<UserRole> findByName(String roleName) {
        RoleEnum roleEnum = RoleEnum.valueOf(roleName);
        String command = "SELECT r FROM UserRole AS r WHERE r.name=:name";
        Query query = em.createQuery(command);
        query.setParameter("name", roleEnum);
        try {
            UserRole role = (UserRole) query.getSingleResult();
            return Optional.of(role);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
