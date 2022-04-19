package bsb.group5.converter.repository.model;

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
@Table(name = "t_conv_application_details")
public class ApplicationDetails {
    @Id
    @GenericGenerator(
            name = "generator",
            strategy = "foreign",
            parameters = @Parameter(name = "property", value = "application")
    )
    @GeneratedValue(generator = "generator")
    @Column(name = "f_application_id", unique = true, nullable = false)
    private Long id;
    @Column(name = "f_create_date", nullable = false)
    private LocalDateTime createDate;
    @Column(name = "f_last_update", nullable = false)
    private LocalDateTime lastUpdate;
    @Column(name = "f_note")
    private String note;
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Application application;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationDetails that = (ApplicationDetails) o;
        return Objects.equals(id, that.id) && Objects.equals(createDate, that.createDate) && Objects.equals(lastUpdate, that.lastUpdate) && Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createDate, lastUpdate, note);
    }

    @Override
    public String toString() {
        return "ApplicationDetails{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                ", note='" + note + '\'' +
                '}';
    }
}
