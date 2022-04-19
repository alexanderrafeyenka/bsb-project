package bsb.group5.auth.repository.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "t_auth_user_session")
public class UserSession {

    @Id
    @Column(name = "f_session_id", unique = true, nullable = false)
    private String sessionId;
    @Column(name = "f_session_start")
    private LocalDateTime sessionStartTime;
    @Column(name = "f_active")
    private boolean isActive = true;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "f_id", nullable = false)
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSession that = (UserSession) o;
        return isActive == that.isActive && Objects.equals(sessionId, that.sessionId) && Objects.equals(sessionStartTime, that.sessionStartTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, sessionStartTime, isActive);
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "sessionId='" + sessionId + '\'' +
                ", sessionStartTime=" + sessionStartTime +
                ", isActive=" + isActive +
                '}';
    }
}
