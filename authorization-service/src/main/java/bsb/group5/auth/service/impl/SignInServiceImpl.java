package bsb.group5.auth.service.impl;

import bsb.group5.auth.repository.UserRepository;
import bsb.group5.auth.repository.UserRoleRepository;
import bsb.group5.auth.repository.model.RoleEnum;
import bsb.group5.auth.repository.model.User;
import bsb.group5.auth.repository.model.UserRole;
import bsb.group5.auth.service.SignInService;
import bsb.group5.auth.service.converters.UserConverter;
import bsb.group5.auth.service.model.SignInUserDTO;
import bsb.group5.auth.service.model.ViewUserDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SignInServiceImpl implements SignInService {
    private final PasswordEncoder passwordEncoder;
    private final UserConverter userConverter;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    @Transactional
    public ViewUserDTO signIn(SignInUserDTO signInUserDTO) {
        User user = userConverter.convertToUser(signInUserDTO);
        UserRole userRole = userRoleRepository.findByName(RoleEnum.ROLE_EMPLOYEE.name()).orElseThrow();
        user.setUserRole(userRole);
        encodePassword(user);
        User addedUser = userRepository.persist(user);
        return userConverter.convertToViewUserDTO(addedUser);
    }

    private void encodePassword(User userToAdd) {
        String passwordToEncode = userToAdd.getPassword();
        String encodedPassword = passwordEncoder.encode(passwordToEncode);
        userToAdd.setPassword(encodedPassword);
    }
}

