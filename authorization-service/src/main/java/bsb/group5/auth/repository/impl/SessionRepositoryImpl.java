package bsb.group5.auth.repository.impl;

import bsb.group5.auth.repository.SessionRepository;
import bsb.group5.auth.repository.model.User;
import bsb.group5.auth.repository.model.UserSession;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class SessionRepositoryImpl extends GenericRepositoryImpl<String, UserSession> implements SessionRepository {
    @Override
    public Optional<List<UserSession>> getActiveSession(User user) {
        String command = "SELECT s FROM UserSession AS s WHERE s.isActive=true AND s.user =: user";
        Query query = em.createQuery(command);
        query.setParameter("user", user);
        try {
            List<UserSession> userSessionList = (List<UserSession>) query.getResultList();
            return Optional.of(userSessionList);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public UserSession findById(String id) {
        String command = "SELECT s FROM UserSession AS s WHERE s.sessionId=:id";
        Query query = em.createQuery(command);
        query.setParameter("id", id);
        try {
            UserSession userSession = (UserSession) query.getSingleResult();
            return userSession;
        } catch (NoResultException e) {
            return null;
        }
    }
}
