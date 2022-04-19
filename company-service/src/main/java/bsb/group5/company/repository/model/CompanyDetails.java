package bsb.group5.company.repository.model;

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
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "t_comp_company_details")
public class CompanyDetails {
    @Id
    @GenericGenerator(
            name = "generator",
            strategy = "foreign",
            parameters = @Parameter(name = "property", value = "company")
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
    private Company company;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyDetails that = (CompanyDetails) o;
        return Objects.equals(companyId, that.companyId) && Objects.equals(createDate, that.createDate) && Objects.equals(lastUpdate, that.lastUpdate) && Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, createDate, lastUpdate, note);
    }

    @Override
    public String toString() {
        return "CompanyDetails{" +
                "companyId=" + companyId +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                ", note='" + note + '\'' +
                '}';
    }
}
