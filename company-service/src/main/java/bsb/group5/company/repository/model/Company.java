package bsb.group5.company.repository.model;

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
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "t_comp_company")
public class Company {
    @Id
    @Column(name = "f_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "f_name_legal", nullable = false, unique = true)
    private String nameLegal;
    @Column(name = "f_unp", nullable = false, unique = true)
    private String unp;
    @Column(name = "f_iban_byn", nullable = false, unique = true)
    private String ibanByByn;
    @Column(name = "f_resident", nullable = false)
    private boolean isResident;
    @Column(name = "f_total_employees", nullable = false)
    private Integer totalEmployees;
    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "company")
    private CompanyDetails companyDetails;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return isResident == company.isResident && Objects.equals(id, company.id) && Objects.equals(nameLegal, company.nameLegal) && Objects.equals(unp, company.unp) && Objects.equals(ibanByByn, company.ibanByByn) && Objects.equals(totalEmployees, company.totalEmployees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameLegal, unp, ibanByByn, isResident, totalEmployees);
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", nameLegal='" + nameLegal + '\'' +
                ", unp=" + unp +
                ", ibanByByn='" + ibanByByn + '\'' +
                ", isResident=" + isResident +
                ", totalEmployees=" + totalEmployees +
                '}';
    }
}
