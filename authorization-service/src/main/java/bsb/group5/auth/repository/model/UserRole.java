package bsb.group5.auth.repository.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@ToString
@Getter
@Setter
@Table(name = "t_auth_user_role")
@EqualsAndHashCode
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "f_id", unique = true, nullable = false)
    private Long id;
    @Column(name = "f_name", unique = true, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RoleEnum name;
}
