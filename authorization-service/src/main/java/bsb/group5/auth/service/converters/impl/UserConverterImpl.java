package bsb.group5.auth.service.converters.impl;

import bsb.group5.auth.repository.UserRoleRepository;
import bsb.group5.auth.repository.model.RoleEnum;
import bsb.group5.auth.repository.model.User;
import bsb.group5.auth.repository.model.UserDetails;
import bsb.group5.auth.repository.model.UserRole;
import bsb.group5.auth.service.converters.UserConverter;
import bsb.group5.auth.service.model.SignInUserDTO;
import bsb.group5.auth.service.model.StatusDTOEnum;
import bsb.group5.auth.service.model.ViewUserDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@Slf4j
@AllArgsConstructor
public class UserConverterImpl implements UserConverter {
    private final UserRoleRepository userRoleRepository;

    @Override
    public User convertToUser(SignInUserDTO signInUserDTO) {
        User user = new User();
        String username = signInUserDTO.getUsername();
        user.setUsername(username);
        String password = signInUserDTO.getPassword();
        user.setPassword(password);
        String email = signInUserDTO.getEmail();
        user.setUserMail(email);
        user.setIsActive(true);
        String firstName = signInUserDTO.getFirstName();
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(firstName);
        userDetails.setUser(user);
        LocalDateTime now = LocalDateTime.now();
        userDetails.setRegistrationDate(now);
        user.setUserDetails(userDetails);
        String roleName = RoleEnum.ROLE_EMPLOYEE.name();
        Optional<UserRole> roleOptional = userRoleRepository.findByName(roleName);
        if (roleOptional.isPresent()) {
            UserRole userRole = roleOptional.get();
            user.setUserRole(userRole);
        } else {
            log.error("Role with name {} not found", roleName);
        }
        return user;
    }

    @Override
    public ViewUserDTO convertToViewUserDTO(User user) {
        Long id = user.getId();
        Boolean isActive = user.getIsActive();
        return ViewUserDTO.builder()
                .id(id)
                .status(StatusDTOEnum.get(isActive))
                .build();
    }
}
