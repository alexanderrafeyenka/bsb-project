package bsb.group5.auth;

import bsb.group5.auth.config.MessageConfig;
import bsb.group5.auth.controllers.AuthController;
import bsb.group5.auth.controllers.handlers.AuthenticationHandler;
import bsb.group5.auth.controllers.handlers.ResponseExceptionHandler;
import bsb.group5.auth.service.LoginService;
import bsb.group5.auth.service.LogoutService;
import bsb.group5.auth.service.SignInService;
import bsb.group5.auth.util.JwtUtil;
import bsb.group5.auth.util.ResponseBodyUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthController.class)
public class JwtRefreshAuthControllerTest {

    private static final String JWT_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbmEiLCJpYXQiOjE2NDc3MjIzMjIsImV4cCI6MT" +
            "k2MzM0MTUyMn0.5tJuYy14oc-XHQS3Biauriwb0Fqf3FwRVVyT1Vn3G1AiOzwSlOPqHs0pDttlSste2zS2o9QLxeNnnXbg6r2Jlg";
    private static final String URL_REFRESH = "/api/auth/refresh";
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MessageConfig messageConfig;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private AuthenticationHandler authenticationHandler;
    @MockBean
    private SignInService signInService;
    @MockBean
    private LoginService loginService;
    @MockBean
    private LogoutService logoutService;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private ResponseExceptionHandler exceptionHandler;
    @MockBean
    private ResponseBodyUtil responseBodyUtil;

    @Test
    public void shouldReturn200WhenPost() throws Exception {
        mvc.perform(post(URL_REFRESH))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void shouldReturnNewTokenWhenValidJwt() throws Exception {
        when(jwtUtil.parseJwt("Bearer " + JWT_TOKEN)).thenReturn(JWT_TOKEN);
        String newToken = jwtUtil.generateJwt("token");
        when(loginService.refreshJwt(JWT_TOKEN)).thenReturn(newToken);
        mvc.perform(post(URL_REFRESH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN))
                .andExpect(status().isOk());
        verify(loginService, times(1)).refreshJwt(JWT_TOKEN);
    }
}
