package bsb.group5.company.service.model;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@ToString
public class GetCompanyDTO {
    private Long ID;
    @Length(max = 255)
    private String Name_Legal;
    @Length(min = 9, max = 9)
    private Integer UNP;
    @Length(min = 28, max = 28)
    private String IBANbyBYN;
    private boolean Type_Legal;
    private Integer Total_Employees;
    private LocalDate Create_date;
    private LocalDate Last_Update;
    private Long applicationId;
    private Long ListId;
    private String Note;

    public GetCompanyDTO(Long ID) {
        this.ID = ID;
    }
}
