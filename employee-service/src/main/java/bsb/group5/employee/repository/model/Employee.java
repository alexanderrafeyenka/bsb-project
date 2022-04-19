package bsb.group5.employee.repository.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "t_empl_employee")
public class Employee {
    @Id
    @Column(name = "f_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "f_full_name", nullable = false, unique = true)
    private String fullName;
    @Column(name = "f_iban_byn", nullable = false, unique = true)
    private String personIbanByn;
    @Column(name = "f_iban_currency", nullable = false, unique = true)
    private String personIbanCurrency;
    @Column(name = "f_recruitment_date", nullable = false)
    private LocalDate recruitmentDate;
    @Column(name = "f_termination_date")
    private LocalDate terminationDate;
    @Column(name = "f_hired", nullable = false)
    private Boolean isHired;
    @Column(name = "f_name_legal", nullable = false)
    private String nameLegal;
    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "employee")
    private EmployeeDetails employeeDetails;
}
