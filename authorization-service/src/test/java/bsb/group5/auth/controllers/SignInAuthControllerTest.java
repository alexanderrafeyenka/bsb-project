package bsb.group5.auth.controllers;


import bsb.group5.auth.controllers.handlers.AuthenticationHandler;
import bsb.group5.auth.repository.UserRepository;
import bsb.group5.auth.repository.model.User;
import bsb.group5.auth.service.LoginService;
import bsb.group5.auth.service.LogoutService;
import bsb.group5.auth.service.SignInService;
import bsb.group5.auth.service.model.SignInUserDTO;
import bsb.group5.auth.service.model.StatusDTOEnum;
import bsb.group5.auth.service.model.ViewUserDTO;
import bsb.group5.auth.util.JwtUtil;
import bsb.group5.auth.util.ResponseBodyUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthController.class)
class SignInAuthControllerTest {
    private static final String URL_TEMPLATE = "/api/auth/signin";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
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
    private ResponseBodyUtil responseBodyUtil;
    @MockBean
    private UserRepository userRepository;


    @Test
    void shouldReturn201WhenWeSignInUsers() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(signinService.signIn(signInUserDTO)).thenReturn(viewUserDTO);
        when(userRepository.getByUsername(signInUserDTO.getUsername())).thenReturn(Optional.empty());
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInUserDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithNoBody() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(signinService.signIn(signInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn415WhenWeSignInUsersWithXMLContentType() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(signinService.signIn(signInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_XML)
                        .content(objectMapper.writeValueAsString(signInUserDTO)))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithNonUniqueUsername() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        User user = new User();
        when(userRepository.getByUsername(signInUserDTO.getUsername())).thenReturn(Optional.of(user));
        when(signinService.signIn(signInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithShortUsername() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .username("user")
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn201WhenWeSignInUsersWithLowerBoundUsername() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .username("userna")
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn201WhenWeSignInUsersWithUpperBoundUsername() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String username = "usernameus".repeat(10);
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .username(username)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithTooLongUsername() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String username = "usernameus".repeat(10) + "u";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .username(username)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithUsernameRussianLayout() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String username = "имяпользователя";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .username(username)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithUsernameDigits() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String username = "user1111";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .username(username)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithEmptyUsername() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String username = "";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .username(username)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithBlankUsername() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String username = "          ";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .username(username)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithTooShortPassword() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String password = "asdfQ1!";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .password(password)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn201WhenWeSignInUsersWithLowerBoundPassword() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String password = "asdfQW1!";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .password(password)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn201WhenWeSignInUsersWithUpperBoundPassword() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String password = "asdfQW1!QW".repeat(2);
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .password(password)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithTooLongPassword() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String password = "asdfQW1!QW".repeat(2) + "q";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .password(password)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithPasswordWithoutUpperCase() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String password = "asdfasdf1!";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .password(password)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithPasswordWithoutLowerCase() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String password = "ASDFASDF1!";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .password(password)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithPasswordWithoutDigits() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String password = "ASDFasdf!";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .password(password)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithPasswordWithoutSpecialSymbols() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String password = "ASDFasdf1";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .password(password)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithEmptyPassword() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String password = "";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .password(password)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithBlankPassword() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String password = "          ";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .password(password)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithEmailWrongPattern() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String email = "sdassaadf";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .email(email)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithEmailAnotherWrongPattern() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String email = "sdas@saadf";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .email(email)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn201WhenWeSignInUsersWithUpperBoundEmail() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String email = "qwertqwert".repeat(9) + "qwe@qwe.qw";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .email(email)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithTooLongEmail() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String email = "qwertqwert".repeat(9) + "qwe@qwe.qw" + "e";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .email(email)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithEmptyEmail() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String email = "";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .email(email)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithBlankEmail() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String email = "            ";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .email(email)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn201WhenWeSignInUsersWithUpperBoundFirstName() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String firstName = "Алекс".repeat(4);
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .firstName(firstName)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithTooLargeFirstName() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String firstName = "Алекс".repeat(4) + "ц";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .firstName(firstName)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithFirstNameEnglishLayout() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String firstName = "АлексD";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .firstName(firstName)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithFirstNameDigits() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String firstName = "Алекс1";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .firstName(firstName)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithFirstNameSpecialSymbols() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String firstName = "Алекс!";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .firstName(firstName)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithEmptyFirstName() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String firstName = "";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .firstName(firstName)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWeSignInUsersWithBlankFirstName() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        String firstName = "         ";
        SignInUserDTO wrongSignInUserDTO = signInUserDTO.toBuilder()
                .firstName(firstName)
                .build();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(wrongSignInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(wrongSignInUserDTO)).thenReturn(viewUserDTO);
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongSignInUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWeSignInInvalidUsersAndMapsMessage() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        User user = new User();
        when(userRepository.getByUsername(signInUserDTO.getUsername())).thenReturn(Optional.of(user));
        when(signinService.signIn(signInUserDTO)).thenReturn(viewUserDTO);
        MvcResult mvcResult = mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInUserDTO)))
                .andExpect(status().isBadRequest()).andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get("errors");
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Not unique", errors.get(0));
    }

    @Test
    void shouldMapsToIdBusinessModelWhenValidInput() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(signInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(signInUserDTO)).thenReturn(viewUserDTO);
        MvcResult mvcResult = mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInUserDTO)))
                .andExpect(status().isCreated()).andReturn();
        verify(signinService, times(1)).signIn(signInUserDTO);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        ViewUserDTO result = objectMapper.readValue(actualResponseBody, ViewUserDTO.class);

        Assertions.assertEquals(1L, result.getId());
    }

    @Test
    void shouldMapsToStatusBusinessModelWhenValidInput() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(signInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(signInUserDTO)).thenReturn(viewUserDTO);
        MvcResult mvcResult = mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInUserDTO)))
                .andExpect(status().isCreated()).andReturn();
        verify(signinService, times(1)).signIn(signInUserDTO);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        ViewUserDTO result = objectMapper.readValue(actualResponseBody, ViewUserDTO.class);

        Assertions.assertEquals(StatusDTOEnum.ACTIVE, result.getStatus());
    }

    @Test
    void shouldReturnUserWhenValidInput() throws Exception {
        SignInUserDTO signInUserDTO = getSigningUserDTO();
        ViewUserDTO viewUserDTO = getViewUserDTO();
        when(userRepository.getByUsername(signInUserDTO.getUsername())).thenReturn(Optional.empty());
        when(signinService.signIn(signInUserDTO)).thenReturn(viewUserDTO);
        MvcResult mvcResult = mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInUserDTO)))
                .andExpect(status().isCreated()).andReturn();
        verify(signinService, times(1)).signIn(signInUserDTO);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(viewUserDTO));
    }

    private ViewUserDTO getViewUserDTO() {
        return ViewUserDTO.builder()
                .id(1L)
                .status(StatusDTOEnum.ACTIVE)
                .build();
    }

    private SignInUserDTO getSigningUserDTO() {
        return SignInUserDTO.builder()
                .username("username")
                .password("passworD1!")
                .email("a.rafyeynka@gmail.com")
                .firstName("Петр")
                .build();
    }
}