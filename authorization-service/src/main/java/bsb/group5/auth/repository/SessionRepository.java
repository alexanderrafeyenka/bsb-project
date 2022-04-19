package bsb.group5.auth.repository;

import bsb.group5.auth.repository.model.User;
import bsb.group5.auth.repository.model.UserSession;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends GenericRepository<String, UserSession> {
    Optional<List<UserSession>> getActiveSession(User user);
}
