package bsb.group5.auth.service;

import bsb.group5.auth.service.model.LogoutUserDTO;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface LogoutService {
    ResponseEntity<Map<String, Object>> logout(LogoutUserDTO logoutUserDTO, String jwt);
}
