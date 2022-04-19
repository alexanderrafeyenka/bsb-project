package bsb.group5.auth.repository;

import bsb.group5.auth.repository.model.User;

import java.util.Optional;

public interface UserRepository extends GenericRepository<Long, User> {

    Optional<User> getByUsername(String username);

    Optional<User> getByUserMail(String userMail);
}
