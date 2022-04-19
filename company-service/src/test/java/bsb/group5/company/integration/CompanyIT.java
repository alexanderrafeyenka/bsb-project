package bsb.group5.company.integration;

import bsb.group5.company.service.model.AddCompanyDTO;
import bsb.group5.company.service.model.TypeLegalDTOEnum;
import bsb.group5.company.service.model.ViewCompanyDTO;
import bsb.group5.company.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CompanyIT extends BaseIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Sql("/sql/add_company.sql")
    void getCompanyById() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(AUTHORIZATION, "Bearer " + jwtUtil.generateJwt("testing", 2000000));
        HttpEntity<Long> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<ViewCompanyDTO> response = restTemplate.exchange(
                createUrlWithPort("/api/legals/{id}"),
                HttpMethod.GET,
                entity,
                ViewCompanyDTO.class,
                33
        );
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertSame(Objects.requireNonNull(response.getBody()).getClass(), ViewCompanyDTO.class);
    }

    @Test
    void addCompany() {
        String nameLegal = "Company333333";
        String ibanByByn = "BY30UNBS00000000000000333333";
        Integer unp = 173333333;
        Integer totalEmployees = 77;

        AddCompanyDTO addCompanyDTO = AddCompanyDTO.builder()
                .nameLegal(nameLegal)
                .unp(unp)
                .ibanByByn(ibanByByn)
                .isResident(true)
                .totalEmployees(totalEmployees)
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, "Bearer " + jwtUtil.generateJwt("User", 2000000));
        HttpEntity<AddCompanyDTO> entity = new HttpEntity<>(addCompanyDTO, httpHeaders);
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
                createUrlWithPort("/api/legals"), entity, Map.class);
        assertEquals(CREATED, responseEntity.getStatusCode());
        assertEquals("Компания успешно создана", responseEntity.getBody().get("message"));
        Map<String, Object> body = (Map<String, Object>) responseEntity.getBody().get("body");
        assertEquals(nameLegal, body.get("Name_Legal"));
        assertEquals(ibanByByn, body.get("IBANbyBYN"));
        assertEquals(unp, body.get("UNP"));
        assertEquals(TypeLegalDTOEnum.Resident.name(), body.get("Type_Legal"));
        assertEquals(totalEmployees, body.get("Total_Employees"));
        assertNotNull(body.get("ID"));
    }

    @Test
    @Sql("/sql/add_company_for_search.sql")
    void searchCompanies() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, "Bearer " + jwtUtil.generateJwt("testing", 2000000));
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String request = "?Name_Legal=Bsb&UNP=807";
        HttpEntity<?> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<List> responseEntity = restTemplate.exchange(
                createUrlWithPort("/api/legals" + request),
                HttpMethod.GET,
                entity,
                List.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @AfterEach
    public void cleanUpTable() {
        jdbcTemplate.execute("TRUNCATE TABLE t_comp_company CASCADE");
    }

    private String createUrlWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
