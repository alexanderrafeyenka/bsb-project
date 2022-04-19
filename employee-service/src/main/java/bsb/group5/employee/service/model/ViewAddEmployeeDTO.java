package bsb.group5.employee.service.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ViewAddEmployeeDTO {
    @JsonProperty("Employee_Id")
    private Long employeeId;
}
