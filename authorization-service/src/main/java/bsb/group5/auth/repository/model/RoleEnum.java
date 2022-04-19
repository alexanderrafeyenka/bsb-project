package bsb.group5.auth.repository.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum RoleEnum {
    ROLE_EMPLOYEE(Set.of("EMPLOYEE")),
    ROLE_ADMIN(Set.of("ADMIN"));

    private Set<String> authorities;

    RoleEnum(Set<String> authorities) {
        this.authorities = authorities;
    }

    public Set<SimpleGrantedAuthority> getAuthority() {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
