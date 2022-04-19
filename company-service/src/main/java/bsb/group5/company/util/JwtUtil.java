package bsb.group5.company.util;

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
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private static final String JWT_PREFIX = "Bearer ";
    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    public String generateJwt(String username, Integer expirationInMs) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expirationInMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey)
                .compact();
    }

    public boolean validateJwt(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token);
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
}
