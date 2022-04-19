package bsb.group5.converter.integration;

import bsb.group5.converter.service.model.ViewApplicationDTO;
import bsb.group5.converter.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ApplicationIT extends BaseIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void addApplicationsByFile() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, "Bearer " + jwtUtil.generateJwt("username", 2000000));
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        File file = new File("src/test/resources/add_applications.csv");
        String absolutePath = file.getAbsolutePath();

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("dtoFile", new FileSystemResource(absolutePath));

        ResponseEntity<ExtendedModelMap> responseEntity = restTemplate.exchange(
                createUrlWithPort("/api/applications"),
                HttpMethod.POST,
                new HttpEntity<>(body, httpHeaders),
                ExtendedModelMap.class
        );
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    @Sql("/sql/add_applications.sql")
    void updateStatusApplication() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, "Bearer " + jwtUtil.generateJwt("testing", 2000000));
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String parameters = "?applicationConvId=c0c4bb28-af62-11ec-b909-0242ac120002&status=InProgress";
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<ViewApplicationDTO> responseEntity = restTemplate.exchange(
                createUrlWithPort("/api/applications" + parameters),
                HttpMethod.PUT,
                entity,
                ViewApplicationDTO.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @Sql("/sql/add_applications.sql")
    void updateNameLegal() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, "Bearer " + jwtUtil.generateJwt("testing", 2000000));
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String parameters = "?Name_Legal=company1";
        HttpEntity<String> entity = new HttpEntity<>(parameters, httpHeaders);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(
                createUrlWithPort("/api/applications/{id}" + parameters),
                HttpMethod.PUT,
                entity,
                Map.class,
                "c0c4bb28-af62-11ec-b909-0242ac120002"
        );
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @Sql("/sql/add_applications.sql")
    void getApplications() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, "Bearer " + jwtUtil.generateJwt("testing", 2000000));
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String parameters = "?page=1";
        HttpEntity<?> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<List> responseEntity = restTemplate.exchange(
                createUrlWithPort("/api/applications" + parameters),
                HttpMethod.GET,
                entity,
                List.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @AfterEach
    public void cleanUpTable() {
        jdbcTemplate.execute("TRUNCATE TABLE t_conv_application CASCADE");
    }

    private String createUrlWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
