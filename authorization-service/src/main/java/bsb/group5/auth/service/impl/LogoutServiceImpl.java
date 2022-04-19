package bsb.group5.auth.service.impl;

import bsb.group5.auth.repository.SessionRepository;
import bsb.group5.auth.repository.UserRepository;
import bsb.group5.auth.repository.model.User;
import bsb.group5.auth.repository.model.UserSession;
import bsb.group5.auth.service.LogoutService;
import bsb.group5.auth.service.model.LogoutUserDTO;
import bsb.group5.auth.util.JwtUtil;
import bsb.group5.auth.util.ResponseBodyUtil;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutServiceImpl implements LogoutService {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final ResponseBodyUtil responseBodyUtil;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public ResponseEntity<Map<String, Object>> logout(LogoutUserDTO logoutUserDTO, String jwt) {
        LocalDateTime expirationDate;
        String usernameFromJwt;
        try {
            usernameFromJwt = jwtUtil.getUsernameFromJwt(jwt);
            expirationDate = jwtUtil.getExpirationDate(jwt);
        } catch (MalformedJwtException e) {
            String message = String.format("JWT token %s is invalid", jwt);
            Map<String, Object> responseBody = responseBodyUtil.getResponseBody(message, HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
        }
        if (expirationDate.isBefore(LocalDateTime.now())) {
            String message = String.format("JWT token %s is expired", jwt);
            Map<String, Object> responseBody = responseBodyUtil.getResponseBody(message, HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
        }
        String username = logoutUserDTO.getUsername();
        if (!username.equals(usernameFromJwt)) {
            String message = "Username is invalid";
            Map<String, Object> responseBody = responseBodyUtil.getResponseBody(message, HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
        }
        Optional<User> byUsername = userRepository.getByUsername(username);
        User user;
        if (byUsername.isEmpty()) {
            String message = String.format("User %s not found", username);
            Map<String, Object> responseBody = responseBodyUtil.getResponseBody(message, HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
        } else {
            user = byUsername.get();
        }
        Optional<List<UserSession>> userSessionListOptional = sessionRepository.getActiveSession(user);
        List<UserSession> userSessionList;
        if (userSessionListOptional.isEmpty()) {
            String message = String.format("User %s hasn't active sessions", username);
            Map<String, Object> responseBody = responseBodyUtil.getResponseBody(message, HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
        } else {
            userSessionList = userSessionListOptional.get();
        }
        userSessionList.stream()
                .map(sessionRepository::persist)
                .forEach(this::setActive);
        String message = String.format("User %s unauthorized", username);
        Map<String, Object> responseBody = responseBodyUtil.getResponseBody(message, HttpStatus.OK);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    private void setActive(UserSession userSession) {
        userSession.setActive(false);
    }
}
