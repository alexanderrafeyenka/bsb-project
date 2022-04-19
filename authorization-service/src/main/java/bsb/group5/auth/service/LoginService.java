package bsb.group5.auth.service;

import bsb.group5.auth.service.exceptions.UserLoginException;
import bsb.group5.auth.service.model.LoginUserDTO;

public interface LoginService {
    String refreshJwt(String jwt) throws UserLoginException;

    String login(LoginUserDTO loginUserDTO) throws UserLoginException;
}
