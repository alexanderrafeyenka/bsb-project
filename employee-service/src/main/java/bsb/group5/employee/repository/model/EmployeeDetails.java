package bsb.group5.employee.repository.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "t_empl_employee_details")
public class EmployeeDetails {
    @Id
    @GenericGenerator(
            name = "generator",
            strategy = "foreign",
            parameters = @Parameter(name = "property", value = "employee")
    )
    @GeneratedValue(generator = "generator")
    @Column(name = "f_id", nullable = false, unique = true)
    private Long companyId;
    @Column(name = "f_create_date", nullable = false)
    private LocalDateTime createDate;
    @Column(name = "f_last_update", nullable = false)
    private LocalDateTime lastUpdate;
    @Column(name = "f_note")
    private String note;
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Employee employee;
}
