package bsb.group5.employee.integration;


import bsb.group5.employee.service.model.AddEmployeeDTO;
import bsb.group5.employee.service.model.ViewGetEmployeeDTO;
import bsb.group5.employee.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EmployeeIT extends BaseIT {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Sql("/sql/add_employee.sql")
    void getEmployeeById() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(AUTHORIZATION, "Bearer " + jwtUtil.generateJwt("testing", 2000000));
        HttpEntity<Long> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<ViewGetEmployeeDTO> response = restTemplate.exchange(
                createUrlWithPort("/api/employees/{id}"),
                HttpMethod.GET,
                entity,
                ViewGetEmployeeDTO.class,
                7
        );
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertSame(Objects.requireNonNull(response.getBody()).getClass(), ViewGetEmployeeDTO.class);
    }

    @Test
    void addEmployee() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, "Bearer " + jwtUtil.generateJwt("employee", 2000000));
        AddEmployeeDTO addEmployeeDTO = getAddEmployeeDTO();

        HttpEntity<AddEmployeeDTO> entity = new HttpEntity<>(addEmployeeDTO, httpHeaders);
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
                createUrlWithPort("/api/employees"), entity, Map.class);
        assertEquals(CREATED, responseEntity.getStatusCode());
        assertEquals("Сотрудник успешно создан", responseEntity.getBody().get("message"));
        Map<String, Object> body = (Map<String, Object>) responseEntity.getBody().get("body");
        assertNotNull(body.get("Employee_Id"));
    }

    @Test
    @Sql("/sql/add_employee_for_search.sql")
    void searchEmployee() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, "Bearer " + jwtUtil.generateJwt("testing", 2000000));
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String request = "?Name_Legal=Bsb&UNP=123";
        HttpEntity<?> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<List> responseEntity = restTemplate.exchange(
                createUrlWithPort("/api/employees" + request),
                HttpMethod.GET,
                entity,
                List.class);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @AfterEach
    public void cleanUpTable() {
        jdbcTemplate.execute("TRUNCATE TABLE t_empl_employee CASCADE");
    }

    private AddEmployeeDTO getAddEmployeeDTO() {
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO();
        addEmployeeDTO.setFullName("Бородуткин Иван Рыгоравич");
        addEmployeeDTO.setNameLegal("Company333333");
        addEmployeeDTO.setPersonIbanByn("BY43UNBS32132131232120004321");
        addEmployeeDTO.setPersonIbanCurrency("BY43UNBS32132131232120004321");
        addEmployeeDTO.setRecruitmentDate(LocalDate.of(2020, 11, 12));
        addEmployeeDTO.setTerminationDate(LocalDate.of(2023, 11, 12));
        return addEmployeeDTO;
    }

    private String createUrlWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
