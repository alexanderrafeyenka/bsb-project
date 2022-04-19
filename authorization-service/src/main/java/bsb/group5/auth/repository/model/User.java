package bsb.group5.auth.repository.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "t_auth_user")
public class User {

    @Id
    @Column(name = "f_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "f_username", nullable = false, unique = true)
    private String username;
    @Column(name = "f_password", nullable = false)
    private String password;
    @Column(name = "f_usermail", nullable = false, unique = true)
    private String userMail;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "f_user_role_id")
    private UserRole userRole;
    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "user")
    private UserDetails userDetails;
    @Column(name = "f_last_login_date")
    private LocalDateTime lastLoginDate;
    @Column(name = "f_logout_date")
    private LocalDateTime logoutDate;
    @Column(name = "f_active")
    private Boolean isActive;
    @Column(name = "f_fail_attempts")
    private Integer failEntryAttempts = 0;
    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "user",
            orphanRemoval = true)
    private Set<UserSession> sessions = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(userMail, user.userMail) && Objects.equals(userRole, user.userRole) && Objects.equals(lastLoginDate, user.lastLoginDate) && Objects.equals(logoutDate, user.logoutDate) && Objects.equals(isActive, user.isActive) && Objects.equals(failEntryAttempts, user.failEntryAttempts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, userMail, userRole, lastLoginDate, logoutDate, isActive, failEntryAttempts);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userMail='" + userMail + '\'' +
                ", userRole=" + userRole +
                ", userDetails=" + userDetails +
                ", lastLoginDate=" + lastLoginDate +
                ", logoutDate=" + logoutDate +
                ", isActive=" + isActive +
                ", failEntryAttempts=" + failEntryAttempts +
                '}';
    }
}
