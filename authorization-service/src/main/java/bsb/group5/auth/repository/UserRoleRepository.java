package bsb.group5.auth.repository;

import bsb.group5.auth.repository.model.UserRole;

import java.util.Optional;

public interface UserRoleRepository extends GenericRepository<Long, UserRole> {
    Optional<UserRole> findByName(String roleName);
}
