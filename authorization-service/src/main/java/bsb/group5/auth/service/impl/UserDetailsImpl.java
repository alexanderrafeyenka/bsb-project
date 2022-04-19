package bsb.group5.auth.service.impl;

import bsb.group5.auth.repository.SessionRepository;
import bsb.group5.auth.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;

@Data
@EqualsAndHashCode
@Slf4j
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final String username;
    private final String password;
    private final Set<SimpleGrantedAuthority> authorities;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        bsb.group5.auth.repository.model.User user = userRepository.getByUsername(username).orElse(null);
        if (user != null) {
            return user.getIsActive();
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static UserDetails build(bsb.group5.auth.repository.model.User userInstance, boolean accountNonLocked) {
        User user = new User(
                userInstance.getUsername(),
                userInstance.getPassword(),
                true,
                true,
                true,
                accountNonLocked,
                userInstance.getUserRole().getName().getAuthority()
        );
        return user;
    }
}
