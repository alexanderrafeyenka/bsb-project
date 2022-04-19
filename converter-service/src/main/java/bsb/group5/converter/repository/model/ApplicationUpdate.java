package bsb.group5.converter.repository.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "t_conv_application_update")
public class ApplicationUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "f_id", unique = true, nullable = false)
    private Long id;
    @Column(name = "f_username", nullable = false)
    private String username;
    @Column(name = "f_changed_field", nullable = false)
    private String changedField;
    @Column(name = "f_value_changed_to", nullable = false)
    private String valueChangedTo;
    @Column(name = "f_update_date", nullable = false)
    private LocalDateTime updateDate = LocalDateTime.now();
    @JoinColumn(name = "f_application_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Application application;
}