package bsb.group5.auth.service.impl;

import bsb.group5.auth.config.MessageConfig;
import bsb.group5.auth.repository.SessionRepository;
import bsb.group5.auth.repository.UserRepository;
import bsb.group5.auth.repository.model.User;
import bsb.group5.auth.repository.model.UserSession;
import bsb.group5.auth.service.LoginService;
import bsb.group5.auth.service.exceptions.UserLoginException;
import bsb.group5.auth.service.model.LoginUserDTO;
import bsb.group5.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final MessageConfig messageConfig;
    private final SessionRepository sessionRepository;
    @Value("${max.entry.attempts}")
    private int maxEntryAttempts;

    @Override
    @Transactional
    public String refreshJwt(String jwt) throws UserLoginException {
        UserSession currentSession = sessionRepository.findById(jwt);
        if (currentSession == null) {
            throw new UserLoginException(messageConfig.getMessageSessionDisabled());
        } else {
            boolean sessionActive = currentSession.isActive();
            if (sessionActive) {
                User user = currentSession.getUser();
                currentSession.setActive(false);
                return getNewSessionId(user);
            }
        }
        throw new UserLoginException(messageConfig.getMessageSessionDisabled());
    }

    @Override
    @Transactional
    public String login(LoginUserDTO loginUserDTO) throws UserLoginException {
        String username = loginUserDTO.getUsername();
        String userMail = loginUserDTO.getUserMail();
        User user;
        if (username != null) {
            user = userRepository.getByUsername(username).orElse(null);
        } else if (userMail != null) {
            user = userRepository.getByUserMail(userMail).orElse(null);
        } else {
            throw new UserLoginException(messageConfig.getMessageUsernamePasswordNotMatch());
        }
        if (user != null) {
            boolean passwordMatches = isPasswordMatches(loginUserDTO, user);
            int failEntryAttempts = user.getFailEntryAttempts();
            if (passwordMatches) {
                user.setFailEntryAttempts(0);
                user.setLastLoginDate(LocalDateTime.now());
                return getNewSessionId(user);
            } else {
                if (!user.getIsActive()) {
                    throw new UserLoginException(messageConfig.getMessageAccountBlocked());
                }
                user.setFailEntryAttempts(++failEntryAttempts);
                if (failEntryAttempts >= maxEntryAttempts) {
                    user.setIsActive(false);
                    throw new UserLoginException(messageConfig.getMessageAccountBlocked());
                }
            }
        }
        throw new UserLoginException(messageConfig.getMessageUsernamePasswordNotMatch());
    }

    private String getNewSessionId(User user) {
        String usernameForJwt = user.getUsername();
        String sessionId = jwtUtil.generateJwt(usernameForJwt);
        UserSession session = getUserSession(user, sessionId);
        sessionRepository.persist(session);
        return sessionId;
    }

    private UserSession getUserSession(User user, String sessionId) {
        UserSession session = new UserSession();
        session.setUser(user);
        session.setSessionId(sessionId);
        session.setSessionStartTime(LocalDateTime.now());
        return session;
    }

    private boolean isPasswordMatches(LoginUserDTO loginUserDTO, User user) {
        String encodedPassword = user.getPassword();
        String rawPassword = loginUserDTO.getPassword();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
