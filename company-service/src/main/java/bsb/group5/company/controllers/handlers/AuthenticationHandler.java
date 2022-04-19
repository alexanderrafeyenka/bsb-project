package bsb.group5.company.controllers.handlers;

import bsb.group5.company.util.ResponseBodyUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;


@Component
@Slf4j
@AllArgsConstructor
public class AuthenticationHandler implements AuthenticationEntryPoint {
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
    private final ResponseBodyUtil responseBodyUtil;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.error("Inauthentic activity: {}", authException.getMessage());
        Map<String, Object> resultBody = responseBodyUtil.getResponseBody(authException.getMessage(), HttpStatus.UNAUTHORIZED);
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ObjectMapper objectMapper = new ObjectMapper();
        try (PrintWriter writer = response.getWriter()) {
            writer.write(objectMapper.writeValueAsString(resultBody));
        }
    }
}