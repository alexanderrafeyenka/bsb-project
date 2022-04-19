package bsb.group5.auth.controllers;

import bsb.group5.auth.service.LoginService;
import bsb.group5.auth.service.LogoutService;
import bsb.group5.auth.service.SignInService;
import bsb.group5.auth.service.exceptions.UserLoginException;
import bsb.group5.auth.service.model.LoginUserDTO;
import bsb.group5.auth.service.model.LogoutUserDTO;
import bsb.group5.auth.service.model.SignInUserDTO;
import bsb.group5.auth.service.model.ViewUserDTO;
import bsb.group5.auth.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {
    private final SignInService signinService;
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final JwtUtil jwtUtil;

    @PostMapping(value = "/signin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ViewUserDTO> signIn(@RequestBody @Validated SignInUserDTO signInUserDTO) {
        ViewUserDTO viewUserDTO = signinService.signIn(signInUserDTO);
        return new ResponseEntity<>(viewUserDTO, HttpStatus.CREATED);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody @Validated LoginUserDTO loginUserDTO) throws UserLoginException {
        String sessionIdByUserDTO = loginService.login(loginUserDTO);
        return new ResponseEntity<>(sessionIdByUserDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> refresh(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization) throws UserLoginException {
        String jwt = jwtUtil.parseJwt(authorization);
        String sessionId = loginService.refreshJwt(jwt);
        return new ResponseEntity<>(sessionId, HttpStatus.OK);
    }

    @PostMapping(value = "/logout", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> logout(
            @RequestBody @Validated LogoutUserDTO logoutUserDTO,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization
    ) {
        String jwt = jwtUtil.parseJwt(authorization);
        return logoutService.logout(logoutUserDTO, jwt);
    }
}
