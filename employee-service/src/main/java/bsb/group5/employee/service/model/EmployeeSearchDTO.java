package bsb.group5.employee.service.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder(toBuilder = true)
public class EmployeeSearchDTO {
    private final String fullName;
    private final String nameLegal;
    private final Integer unp;
}
