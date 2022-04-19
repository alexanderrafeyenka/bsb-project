package bsb.group5.converter.controllers.filters;

import bsb.group5.converter.config.MessageConfig;
import bsb.group5.converter.util.JwtUtil;
import bsb.group5.converter.util.ResponseBodyUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
    private final JwtUtil jwtUtil;
    private final MessageConfig messageConfig;
    private final ResponseBodyUtil responseBodyUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {
        String jwt = jwtUtil.parseJwt(request);
        if (jwt != null && jwtUtil.validateJwt(jwt)) {
            filterChain.doFilter(request, response);
        } else {
            log.error(messageConfig.getMessageAuthenticationException());
            Map<String, Object> responseBody = responseBodyUtil
                    .getResponseBody(messageConfig.getMessageAuthenticationException(), HttpStatus.UNAUTHORIZED);
            response.setContentType(CONTENT_TYPE);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            ObjectMapper objectMapper = new ObjectMapper();
            try (PrintWriter writer = response.getWriter()) {
                writer.write(objectMapper.writeValueAsString(responseBody));
            }
        }
    }
}
