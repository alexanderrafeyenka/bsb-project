package bsb.group5.auth.repository.impl;

import bsb.group5.auth.repository.UserRepository;
import bsb.group5.auth.repository.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Optional;

@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl<Long, User> implements UserRepository {
    @Override
    public Optional<User> getByUsername(String username) {
        String command = "SELECT u FROM User AS u WHERE u.username=:username";
        Query query = em.createQuery(command);
        query.setParameter("username", username);
        try {
            User user = (User) query.getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getByUserMail(String userMail) {
        String command = "SELECT u FROM User AS u WHERE u.userMail=:userMail";
        Query query = em.createQuery(command);
        query.setParameter("userMail", userMail);
        try {
            User user = (User) query.getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
