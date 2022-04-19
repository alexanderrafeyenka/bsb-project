package bsb.group5.auth.integration;


import bsb.group5.auth.service.model.LoginUserDTO;
import bsb.group5.auth.service.model.LogoutUserDTO;
import bsb.group5.auth.service.model.SignInUserDTO;
import bsb.group5.auth.service.model.StatusDTOEnum;
import bsb.group5.auth.service.model.ViewUserDTO;
import bsb.group5.auth.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthorizationIT extends BaseIT {

    private final static String JWT = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0YWRtaW4iLCJpYXQiOjE2NDg1MDM4MTMsImV4cCI6MjI3OTY1NTgxM30.NC7xJQ3S3ECCNg1MRwuakc3axqi0Mu45gugS0YlXIKBgouIvrtlrsY1WpgbJJ6YCjuq0l9hd5v8ELOMtwyx5Vg";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Sql("/sql/login.sql")
    void login() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        LoginUserDTO loginUserDTO = LoginUserDTO.builder()
                .username("usernametest")
                .password("Password3!")
                .build();

        HttpEntity<LoginUserDTO> entity = new HttpEntity<>(loginUserDTO, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                createUrlWithPort("/api/auth/login"), entity, String.class);
        assertEquals(responseEntity.getStatusCode(), OK);
        assertSame(Objects.requireNonNull(responseEntity.getBody()).getClass(), String.class);
        assertTrue(jwtUtil.validateJwt(responseEntity.getBody()));
    }

    @Test
    void signIn() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        SignInUserDTO signInUserDTO = SignInUserDTO.builder()
                .username("usernameunique")
                .password("Password3!")
                .email("username@bsb.by")
                .firstName("Пользователь")
                .build();

        HttpEntity<SignInUserDTO> entity = new HttpEntity<>(signInUserDTO, httpHeaders);
        ResponseEntity<ViewUserDTO> responseEntity = restTemplate.postForEntity(
                createUrlWithPort("/api/auth/signin"), entity, ViewUserDTO.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
        assertEquals(responseEntity.getBody().getClass(), ViewUserDTO.class);
        assertEquals(responseEntity.getBody().getStatus(), StatusDTOEnum.ACTIVE);
        assertNotNull(responseEntity.getBody().getId());
    }

    @Test
    @Sql("/sql/refresh.sql")
    void refreshToken() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, "Bearer " + JWT);
        HttpEntity<?> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                createUrlWithPort("/api/auth/refresh"), entity, String.class);
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(String.class, responseEntity.getBody().getClass());
        assertTrue(jwtUtil.validateJwt(responseEntity.getBody()));
    }

    @Test
    @Sql("/sql/logout.sql")
    void logout() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, "Bearer " + JWT);
        LogoutUserDTO logoutUserDTO = new LogoutUserDTO("testadmin");
        HttpEntity<LogoutUserDTO> entity = new HttpEntity<>(logoutUserDTO, httpHeaders);
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(createUrlWithPort("/api/auth/logout"), entity, Map.class);
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(LinkedHashMap.class, responseEntity.getBody().getClass());
        assertEquals("User testadmin unauthorized", responseEntity.getBody().get("message"));
    }

    @AfterEach
    public void cleanUpTable() {
        jdbcTemplate.execute("TRUNCATE TABLE t_auth_user CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE t_auth_user_session CASCADE");
    }

    private String createUrlWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
