package bsb.group5.auth.service;

import bsb.group5.auth.service.model.SignInUserDTO;
import bsb.group5.auth.service.model.ViewUserDTO;

public interface SignInService {
    ViewUserDTO signIn(SignInUserDTO signInUserDTO);
}
