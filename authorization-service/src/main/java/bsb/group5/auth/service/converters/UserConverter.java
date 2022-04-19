package bsb.group5.auth.service.converters;

import bsb.group5.auth.repository.model.User;
import bsb.group5.auth.service.model.SignInUserDTO;
import bsb.group5.auth.service.model.ViewUserDTO;

public interface UserConverter {
    User convertToUser(SignInUserDTO signInUserDTO);

    ViewUserDTO convertToViewUserDTO(User user);
}
