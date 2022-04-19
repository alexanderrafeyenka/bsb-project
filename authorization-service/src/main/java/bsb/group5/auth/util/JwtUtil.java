package bsb.group5.auth.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private static final String JWT_PREFIX = "Bearer ";
    @Value("${jwt.secret.key}")
    private String jwtSecretKey;
    @Value("${jwt.expiration.ms}")
    private long jwtExpirationMs;

    public String generateJwt(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey)
                .compact();
    }

    public String getUsernameFromJwt(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public LocalDateTime getExpirationDate(String token) {
        Date expirationDate = Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return Instant.ofEpochMilli(expirationDate.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public boolean validateJwt(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecretKey)
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Invalid JWT expire date {}", e.getMessage(), e);
        } catch (UnsupportedJwtException e) {
            log.error("Invalid JWT format {}", e.getMessage(), e);
        } catch (MalformedJwtException e) {
            log.error("JWT was not correctly constructed {}", e.getMessage(), e);
        } catch (SignatureException e) {
            log.error("JWT has invalid signature {}", e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            log.error("Invalid argument {}", e.getMessage(), e);
        }
        return false;
    }

    public String parseJwt(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authorization) && authorization.startsWith(JWT_PREFIX)) {
            return authorization.substring(JWT_PREFIX.length());
        }
        return null;
    }

    public String parseJwt(String authorization) {
        if (StringUtils.hasText(authorization) && authorization.startsWith(JWT_PREFIX)) {
            return authorization.substring(JWT_PREFIX.length());
        }
        return null;
    }
}
