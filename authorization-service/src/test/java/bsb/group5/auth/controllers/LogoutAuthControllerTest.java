package bsb.group5.auth.controllers;

import bsb.group5.auth.controllers.handlers.AuthenticationHandler;
import bsb.group5.auth.repository.UserRepository;
import bsb.group5.auth.service.LoginService;
import bsb.group5.auth.service.LogoutService;
import bsb.group5.auth.service.SignInService;
import bsb.group5.auth.service.model.LogoutUserDTO;
import bsb.group5.auth.util.JwtUtil;
import bsb.group5.auth.util.ResponseBodyUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthController.class)
class LogoutAuthControllerTest {
    private static final String URL_TEMPLATE = "/api/auth/logout";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ResponseBodyUtil responseBodyUtil;
    @MockBean
    private SignInService signinService;
    @MockBean
    private LoginService loginService;
    @MockBean
    private LogoutService logoutService;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private AuthenticationHandler authenticationHandler;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private UserRepository userRepository;


    @Test
    @WithMockUser(password = "pass", username = "username")
    void shouldReturn200WhenWeLogoutUsers() throws Exception {
        LogoutUserDTO logoutUserDTO = getLogoutUserDTO();
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity();
        String jwt = getJwt();
        when(logoutService.logout(logoutUserDTO, jwt)).thenReturn(responseEntity);
        mockMvc.perform(post(URL_TEMPLATE)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutUserDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(password = "pass", username = "username")
    void shouldReturn401WhenWeLogoutUsersWithWrongHeader() throws Exception {
        LogoutUserDTO logoutUserDTO = getLogoutUserDTO();
        ResponseEntity<Map<String, Object>> responseEntity = getWrongResponseEntity();
        String jwt = getJwt() + "df";
        String authorization = "Bearer" + jwt;
        when(logoutService.logout(logoutUserDTO, jwt)).thenReturn(responseEntity);
        when(jwtUtil.parseJwt(authorization)).thenReturn(jwt);
        mockMvc.perform(post(URL_TEMPLATE)
                        .header(HttpHeaders.AUTHORIZATION, authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutUserDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(password = "pass", username = "username")
    void shouldReturn400WhenWeLogoutUsersWithoutHeader() throws Exception {
        LogoutUserDTO logoutUserDTO = getLogoutUserDTO();
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity();
        String jwt = getJwt();
        when(logoutService.logout(logoutUserDTO, jwt)).thenReturn(responseEntity);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutUserDTO)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(password = "pass", username = "username")
    void shouldReturn415WhenWeLogoutUsersWithXMLContentType() throws Exception {
        LogoutUserDTO logoutUserDTO = getLogoutUserDTO();
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity();
        String jwt = getJwt();
        String authorization = "Bearer " + jwt;
        when(logoutService.logout(logoutUserDTO, jwt)).thenReturn(responseEntity);
        mockMvc.perform(post(URL_TEMPLATE)
                        .header(HttpHeaders.AUTHORIZATION, authorization)
                        .contentType(MediaType.APPLICATION_XML)
                        .content(objectMapper.writeValueAsString(logoutUserDTO)))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @WithMockUser(password = "pass", username = "username")
    void shouldReturn400WhenWeLogoutUsersWithNoBody() throws Exception {
        String jwt = getJwt();
        String authorization = "Bearer " + jwt;
        mockMvc.perform(post(URL_TEMPLATE)
                        .header(HttpHeaders.AUTHORIZATION, authorization)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(password = "pass", username = "username")
    void shouldReturn401WhenWeLogoutUsersWithExpiredToken() throws Exception {
        LogoutUserDTO logoutUserDTO = getLogoutUserDTO();
        ResponseEntity<Map<String, Object>> responseEntity = getWrongResponseEntity();
        String jwt = getExpiredJwt();
        String authorization = "Bearer " + jwt;
        when(logoutService.logout(logoutUserDTO, jwt)).thenReturn(responseEntity);
        when(jwtUtil.parseJwt(authorization)).thenReturn(jwt);
        mockMvc.perform(post(URL_TEMPLATE)
                        .header(HttpHeaders.AUTHORIZATION, authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutUserDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(password = "pass", username = "username")
    void shouldReturn401WhenWeLogoutUsersWithWrongUsername() throws Exception {
        LogoutUserDTO logoutUserDTO = new LogoutUserDTO("asdfasdf");
        ResponseEntity<Map<String, Object>> responseEntity = getWrongResponseEntity();
        String jwt = getJwt();
        String authorization = "Bearer " + jwt;
        when(logoutService.logout(logoutUserDTO, jwt)).thenReturn(responseEntity);
        when(jwtUtil.parseJwt(authorization)).thenReturn(jwt);
        mockMvc.perform(post(URL_TEMPLATE)
                        .header(HttpHeaders.AUTHORIZATION, authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutUserDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(password = "pass", username = "username")
    void shouldReturn401WhenWeLogoutUsersWithNotExistUsername() throws Exception {
        LogoutUserDTO logoutUserDTO = new LogoutUserDTO("asdfasdf");
        ResponseEntity<Map<String, Object>> responseEntity = getWrongResponseEntity();
        String jwt = getJwt();
        String authorization = "Bearer " + jwt;
        when(logoutService.logout(logoutUserDTO, jwt)).thenReturn(responseEntity);
        when(jwtUtil.parseJwt(authorization)).thenReturn(jwt);
        mockMvc.perform(post(URL_TEMPLATE)
                        .header(HttpHeaders.AUTHORIZATION, authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutUserDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(password = "pass", username = "username")
    void shouldReturn400WhenWeLogoutUsersWithTooShortUsername() throws Exception {
        LogoutUserDTO logoutUserDTO = new LogoutUserDTO("qwert");
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity();
        String jwt = getJwt();
        String authorization = "Bearer " + jwt;
        when(logoutService.logout(logoutUserDTO, jwt)).thenReturn(responseEntity);
        when(jwtUtil.parseJwt(authorization)).thenReturn(jwt);
        mockMvc.perform(post(URL_TEMPLATE)
                        .header("Authorization", authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(password = "pass", username = "username")
    void shouldReturn200WhenWeLogoutUsersWithLowerBoundUsername() throws Exception {
        LogoutUserDTO logoutUserDTO = new LogoutUserDTO("qwerty");
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity();
        String jwt = getJwt();
        String authorization = "Bearer " + jwt;
        when(logoutService.logout(logoutUserDTO, jwt)).thenReturn(responseEntity);
        when(jwtUtil.parseJwt(authorization)).thenReturn(jwt);
        mockMvc.perform(post(URL_TEMPLATE)
                        .header(HttpHeaders.AUTHORIZATION, authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutUserDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(password = "pass", username = "username")
    void shouldReturn200WhenWeLogoutUsersWithUpperBoundUsername() throws Exception {
        String username = "qwert".repeat(20);
        LogoutUserDTO logoutUserDTO = new LogoutUserDTO(username);
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity();
        String jwt = getJwt();
        String authorization = "Bearer " + jwt;
        when(logoutService.logout(logoutUserDTO, jwt)).thenReturn(responseEntity);
        when(jwtUtil.parseJwt(authorization)).thenReturn(jwt);
        mockMvc.perform(post(URL_TEMPLATE)
                        .header(HttpHeaders.AUTHORIZATION, authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutUserDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(password = "pass", username = "username")
    void shouldReturn400WhenWeLogoutUsersWithTooLongUsername() throws Exception {
        String username = "qwert".repeat(20) + "q";
        LogoutUserDTO logoutUserDTO = new LogoutUserDTO(username);
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity();
        String jwt = getJwt();
        String authorization = "Bearer " + jwt;
        when(logoutService.logout(logoutUserDTO, jwt)).thenReturn(responseEntity);
        when(jwtUtil.parseJwt(authorization)).thenReturn(jwt);
        mockMvc.perform(post(URL_TEMPLATE)
                        .header(HttpHeaders.AUTHORIZATION, authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(password = "pass", username = "username")
    void shouldReturn400WhenWeLogoutUsersWithWithUsernameRussianLayout() throws Exception {
        LogoutUserDTO logoutUserDTO = new LogoutUserDTO("фывапр");
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity();
        String jwt = getJwt();
        String authorization = "Bearer " + jwt;
        when(logoutService.logout(logoutUserDTO, jwt)).thenReturn(responseEntity);
        when(jwtUtil.parseJwt(authorization)).thenReturn(jwt);
        mockMvc.perform(post(URL_TEMPLATE)
                        .header(HttpHeaders.AUTHORIZATION, authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(password = "pass", username = "username")
    void shouldReturn400WhenWeLogoutUsersWithWithUsernameDigits() throws Exception {
        LogoutUserDTO logoutUserDTO = new LogoutUserDTO("asdfggh1");
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity();
        String jwt = getJwt();
        String authorization = "Bearer " + jwt;
        when(logoutService.logout(logoutUserDTO, jwt)).thenReturn(responseEntity);
        when(jwtUtil.parseJwt(authorization)).thenReturn(jwt);
        mockMvc.perform(post(URL_TEMPLATE)
                        .header(HttpHeaders.AUTHORIZATION, authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(password = "pass", username = "username")
    void shouldReturn400WhenWeLogoutUsersWithWithEmptyUsername() throws Exception {
        LogoutUserDTO logoutUserDTO = new LogoutUserDTO("");
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity();
        String jwt = getJwt();
        String authorization = "Bearer " + jwt;
        when(logoutService.logout(logoutUserDTO, jwt)).thenReturn(responseEntity);
        when(jwtUtil.parseJwt(authorization)).thenReturn(jwt);
        mockMvc.perform(post(URL_TEMPLATE)
                        .header(HttpHeaders.AUTHORIZATION, authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(password = "pass", username = "username")
    void shouldReturn400WhenWeLogoutUsersWithWithBlankUsername() throws Exception {
        LogoutUserDTO logoutUserDTO = new LogoutUserDTO("                 ");
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity();
        String jwt = getJwt();
        String authorization = "Bearer " + jwt;
        when(logoutService.logout(logoutUserDTO, jwt)).thenReturn(responseEntity);
        when(jwtUtil.parseJwt(authorization)).thenReturn(jwt);
        mockMvc.perform(post(URL_TEMPLATE)
                        .header(HttpHeaders.AUTHORIZATION, authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(password = "pass", username = "username")
    void shouldReturn400WhenWeLogoutUsersWithUsernameUpperCase() throws Exception {
        LogoutUserDTO logoutUserDTO = new LogoutUserDTO("QWERTYU");
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity();
        String jwt = getJwt();
        String authorization = "Bearer " + jwt;
        when(logoutService.logout(logoutUserDTO, jwt)).thenReturn(responseEntity);
        when(jwtUtil.parseJwt(authorization)).thenReturn(jwt);
        mockMvc.perform(post(URL_TEMPLATE)
                        .header(HttpHeaders.AUTHORIZATION, authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(password = "pass", username = "username")
    void shouldReturn400WhenWeLogoutUsersWithUsernameSpecialSymbols() throws Exception {
        LogoutUserDTO logoutUserDTO = new LogoutUserDTO("qwerty!");
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity();
        String jwt = getJwt();
        String authorization = "Bearer " + jwt;
        when(logoutService.logout(logoutUserDTO, jwt)).thenReturn(responseEntity);
        when(jwtUtil.parseJwt(authorization)).thenReturn(jwt);
        mockMvc.perform(post(URL_TEMPLATE)
                        .header(HttpHeaders.AUTHORIZATION, authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(password = "pass", username = "username")
    void shouldReturn200AndVerifyLogoutServiceWhenWeLogoutUsers() throws Exception {
        LogoutUserDTO logoutUserDTO = getLogoutUserDTO();
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity();
        String jwt = getJwt();
        String authorization = "Bearer " + jwt;
        when(logoutService.logout(logoutUserDTO, jwt)).thenReturn(responseEntity);
        when(jwtUtil.parseJwt(authorization)).thenReturn(jwt);
        mockMvc.perform(post(URL_TEMPLATE)
                        .header(HttpHeaders.AUTHORIZATION, authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutUserDTO)))
                .andExpect(status().isOk());
        verify(logoutService, times(1)).logout(logoutUserDTO, jwt);
    }

    @Test
    @WithMockUser(password = "pass", username = "username")
    void shouldReturn200AndVerifyJwtUtilWhenWeLogoutUsers() throws Exception {
        LogoutUserDTO logoutUserDTO = getLogoutUserDTO();
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity();
        String jwt = getJwt();
        String authorization = "Bearer " + jwt;
        when(logoutService.logout(logoutUserDTO, jwt)).thenReturn(responseEntity);
        when(jwtUtil.parseJwt(authorization)).thenReturn(jwt);
        mockMvc.perform(post(URL_TEMPLATE)
                        .header(HttpHeaders.AUTHORIZATION, authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutUserDTO)))
                .andExpect(status().isOk());
        verify(jwtUtil, times(1)).parseJwt(authorization);
    }

    @Test
    @WithMockUser(password = "pass", username = "username")
    void shouldReturn400WhenWeLogoutInvalidUsersAndMapsMessage() throws Exception {
        LogoutUserDTO logoutUserDTO = new LogoutUserDTO("qwerty!");
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity();
        String jwt = getJwt();
        String authorization = "Bearer " + jwt;
        when(logoutService.logout(logoutUserDTO, jwt)).thenReturn(responseEntity);
        when(jwtUtil.parseJwt(authorization)).thenReturn(jwt);
        MvcResult mvcResult = mockMvc.perform(post(URL_TEMPLATE)
                        .header(HttpHeaders.AUTHORIZATION, authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutUserDTO)))
                .andExpect(status().isBadRequest()).andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get("errors");
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Username is invalid", errors.get(0));
    }

    @Test
    @WithMockUser(password = "pass", username = "username")
    void shouldMapsToBusinessModelWhenValidInput() throws Exception {
        LogoutUserDTO logoutUserDTO = getLogoutUserDTO();
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity();
        System.out.println(responseEntity);
        String jwt = getJwt();
        String authorization = "Bearer " + jwt;
        when(logoutService.logout(logoutUserDTO, jwt)).thenReturn(responseEntity);
        when(jwtUtil.parseJwt(authorization)).thenReturn(jwt);
        MvcResult mvcResult = mockMvc.perform(post(URL_TEMPLATE)
                        .header(HttpHeaders.AUTHORIZATION, authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutUserDTO)))
                .andExpect(status().isOk()).andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        System.out.println(map);
        Assertions.assertEquals("User username unauthorized", map.get("message"));
    }

    @Test
    @WithMockUser(password = "pass", username = "username")
    void shouldReturnResponseEntityWhenValidInput() throws Exception {
        LogoutUserDTO logoutUserDTO = getLogoutUserDTO();
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity();
        String jwt = getJwt();
        String authorization = "Bearer " + jwt;
        when(logoutService.logout(logoutUserDTO, jwt)).thenReturn(responseEntity);
        when(jwtUtil.parseJwt(authorization)).thenReturn(jwt);
        MvcResult mvcResult = mockMvc.perform(post(URL_TEMPLATE)
                        .header(HttpHeaders.AUTHORIZATION, authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutUserDTO)))
                .andExpect(status().isOk()).andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(responseEntity.getBody()));
    }

    private ResponseEntity<Map<String, Object>> getResponseEntity() {
        Map<String, Object> resultBody = new LinkedHashMap<>();
        resultBody.put("timestamp", String.valueOf(LocalDateTime.now()));
        resultBody.put("status", HttpStatus.OK);
        resultBody.put("message", "User username unauthorized");
        return new ResponseEntity<>(resultBody, HttpStatus.OK);
    }

    private ResponseEntity<Map<String, Object>> getWrongResponseEntity() {
        Map<String, Object> responseBody = responseBodyUtil.getResponseBody(
                "message", HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
    }

    private LogoutUserDTO getLogoutUserDTO() {
        return new LogoutUserDTO("username");
    }

    private String getJwt() {
        return jwtUtil.generateJwt("username");
    }

    private String getExpiredJwt() {
        return Jwts.builder()
                .setSubject("username")
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() - 1000000L))
                .signWith(SignatureAlgorithm.HS512, "secret")
                .compact();
    }
}