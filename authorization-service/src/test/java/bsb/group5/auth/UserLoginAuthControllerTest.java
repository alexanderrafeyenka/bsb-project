package bsb.group5.auth;

import bsb.group5.auth.config.MessageConfig;
import bsb.group5.auth.controllers.AuthController;
import bsb.group5.auth.controllers.handlers.AuthenticationHandler;
import bsb.group5.auth.repository.SessionRepository;
import bsb.group5.auth.repository.UserRepository;
import bsb.group5.auth.repository.model.User;
import bsb.group5.auth.service.LoginService;
import bsb.group5.auth.service.LogoutService;
import bsb.group5.auth.service.SignInService;
import bsb.group5.auth.service.exceptions.UserLoginException;
import bsb.group5.auth.service.model.LoginUserDTO;
import bsb.group5.auth.util.JwtUtil;
import bsb.group5.auth.util.ResponseBodyUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthController.class)
public class UserLoginAuthControllerTest {

    private static final String JWT_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY0NjY2Nzc2NiwiZXhwIjoxNjQ2OTI2OTY2fQ." +
            "_sWMzLJlqH6OBP6iA50G_qvwb3cbeiY5veocndwDHnorcKtgeBMXAIHbEeYVBSXCpMX5Gp4JPN8uakTxRhlC4Q";
    private static final String URL_LOGIN = "/api/auth/login";
    private static final String messageInvalidLoginPassword = "Invalid login or password provided";
    private static final String messageAccountBlocked = "Ваша учетная запись заблокирована. Обратитесь к Администратору";
    private static final String messageUsernamePasswordNotMatch = "Username does not match password";
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private AuthenticationHandler authenticationHandler;
    @MockBean
    private SessionRepository sessionRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private SignInService signInService;
    @MockBean
    private LoginService loginService;
    @MockBean
    private LogoutService logoutService;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private ResponseBodyUtil responseBodyUtil;
    @MockBean
    private MessageConfig messageConfig;


    @Test
    public void shouldReturn200WhenValidUsernameDTOJSON() throws Exception {
        LoginUserDTO loginUserDTO = getLoginUserDTO("username", null, "pass");
        mvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn415WhenXML() throws Exception {
        mvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void shouldReturn415WhenSendParameters() throws Exception {
        mvc.perform(post(URL_LOGIN + "?username=name&password=pass"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void shouldReturn200WhenValidUserMailDTOJSON() throws Exception {
        LoginUserDTO loginUserDTO = getLoginUserDTO(null, "admin@bsb.by", "pass");
        mvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn400WhenPasswordBlank() throws Exception {
        LoginUserDTO loginUserDTO = getLoginUserDTO(null, "admin@bsb.by", "");
        mvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n", "U", "5", "админ", "admin_"})
    public void shouldReturn400WhenNotValidUsernameFormat(String input) throws Exception {
        LoginUserDTO loginUserDTO = getLoginUserDTO(input, null, "pass");
        mvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"[", "\t", "\n", "админ", ")"})
    public void shouldReturn400WhenNotValidMailFormat(String input) throws Exception {
        LoginUserDTO loginUserDTO = getLoginUserDTO(null, input + "@bsb.by", "pass");
        mvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserDTO)))
                .andExpect(status().isBadRequest());
    }


    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"}", "/", "$", "#", "!", "*", "&", "^"})
    public void shouldCastBusinessWhenSpecialSymbolsEmail(String input) throws Exception {
        LoginUserDTO loginUserDTO = getLoginUserDTO(null, input + "email@bsb.by", "pass");
        when(loginService.login(loginUserDTO)).thenReturn(JWT_TOKEN);
        MvcResult mvcResult = mvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserDTO)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        verify(loginService, times(1)).login(loginUserDTO);
        assertThat(actualResponse).contains(JWT_TOKEN);
    }


    @Test
    public void shouldCastBusinessWhenLogInUserWithEmail() throws Exception {
        LoginUserDTO loginUserDTO = getLoginUserDTO(null, "admin@bsb.by", "pass");
        when(loginService.login(loginUserDTO)).thenReturn(JWT_TOKEN);
        MvcResult mvcResult = mvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserDTO)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        verify(loginService, times(1)).login(loginUserDTO);
        assertThat(actualResponse).contains(JWT_TOKEN);
    }

    @Test
    public void shouldCastBusinessWhenLogInUserWithUsername() throws Exception {
        LoginUserDTO loginUserDTO = getLoginUserDTO("admin", null, "pass");
        when(loginService.login(loginUserDTO)).thenReturn(JWT_TOKEN);
        MvcResult mvcResult = mvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserDTO)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        verify(loginService, times(1)).login(loginUserDTO);
        assertThat(actualResponse).contains(JWT_TOKEN);
    }

    @Test
    public void shouldReturn400AndMessageWhenUsernameAndPasswordDoNotMatch() throws Exception {
        LoginUserDTO loginUserDTO = getLoginUserDTO("user", null, "password");
        User user = new User();
        when(loginService.login(loginUserDTO)).thenThrow(new UserLoginException(messageUsernamePasswordNotMatch));
        MvcResult mvcResult = mvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        verify(loginService, times(1)).login(loginUserDTO);
        assertThat(actualResponse).contains(messageUsernamePasswordNotMatch);
    }

    @Test
    public void shouldReturnAccountBlockedWhenPasswordDoNotMatch() throws Exception {
        LoginUserDTO loginUserDTO = getLoginUserDTO("user", null, "password");
        when(loginService.login(loginUserDTO)).thenThrow(new UserLoginException(messageAccountBlocked));
        MvcResult mvcResult = mvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        verify(loginService, times(1)).login(loginUserDTO);
        assertThat(actualResponse).contains(messageAccountBlocked);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"admin@bsbby", "admin@bsb.", "adminbsb.by", "@bsb.by", "admin@.by", "admin@bsbby.", "admin@bsb.by.", ".admin@bsb.by", "admin.@bsb.by"})
    public void shouldReturn400AndMessageWhenInvalidMailFormat(String input) throws Exception {
        LoginUserDTO loginUserDTO = getLoginUserDTO(null, input, "pass");
        MvcResult mvcResult = mvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        Map<String, Object> map = objectMapper.readValue(actualResponse, Map.class);
        List<String> errors = (List<String>) map.get("errors");
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals(messageInvalidLoginPassword, errors.get(0));
    }

    @Test
    public void shouldReturn400AndMessageWhenEmptyUsernameAndUserMail() throws Exception {
        LoginUserDTO loginUserDTO = getLoginUserDTO("", null, "pass");
        MvcResult mvcResult = mvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        Map<String, Object> map = objectMapper.readValue(actualResponse, Map.class);
        List<String> errors = (List<String>) map.get("errors");
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals(messageInvalidLoginPassword, errors.get(0));
    }

    @Test
    public void shouldReturn400AndMessageWhenPasswordBlank() throws Exception {
        LoginUserDTO loginUserDTO = getLoginUserDTO("username", null, "");
        MvcResult mvcResult = mvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        Map<String, Object> map = objectMapper.readValue(actualResponse, Map.class);
        List<String> errors = (List<String>) map.get("errors");
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals(messageInvalidLoginPassword, errors.get(0));
    }

    @Test
    public void shouldReturn400AndMessageWhenBothUsernameAndMail() throws Exception {
        LoginUserDTO loginUserDTO = getLoginUserDTO("username", "admin@bsb.by", "pass");
        MvcResult mvcResult = mvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        Map<String, Object> map = objectMapper.readValue(actualResponse, Map.class);
        List<String> errors = (List<String>) map.get("errors");
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals(messageInvalidLoginPassword, errors.get(0));
    }

    private LoginUserDTO getLoginUserDTO(String username, String userMail, String pass) {
        return LoginUserDTO.builder()
                .username(username)
                .userMail(userMail)
                .password(pass)
                .build();
    }
}
