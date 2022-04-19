package bsb.group5.converter.repository.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "t_conv_application")
@ToString
public class Application {
    @Id
    @Column(name = "f_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "f_uuid", nullable = false, unique = true)
    private UUID uuid;
    @Column(name = "f_status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StatusEnum status;
    @Column(name = "f_employee_id", nullable = false)
    private Long employeeId;
    @Column(name = "f_name_legal", nullable = false)
    private String nameLegal;
    @Column(name = "f_convert_from", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CurrencyEnum currencyConvertFrom;
    @Column(name = "f_convert_to", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CurrencyEnum currencyConvertTo;
    @Column(name = "f_percent_conv", nullable = false)
    private Float percentConv;
    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "application")
    private ApplicationDetails applicationDetails;
    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "application")
    private List<ApplicationUpdate> applicationUpdates = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Application that = (Application) o;
        return Objects.equals(id, that.id) && Objects.equals(uuid, that.uuid) && status == that.status && Objects.equals(employeeId, that.employeeId) && Objects.equals(nameLegal, that.nameLegal) && currencyConvertFrom == that.currencyConvertFrom && currencyConvertTo == that.currencyConvertTo && Objects.equals(percentConv, that.percentConv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, status, employeeId, nameLegal, currencyConvertFrom, currencyConvertTo, percentConv);
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", status=" + status +
                ", employeeId=" + employeeId +
                ", nameLegal='" + nameLegal + '\'' +
                ", currencyConvertFrom=" + currencyConvertFrom +
                ", currencyConvertTo=" + currencyConvertTo +
                ", percentConv=" + percentConv +
                '}';
    }
}
