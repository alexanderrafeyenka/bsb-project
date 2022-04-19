package bsb.group5.company.service.model;

import bsb.group5.company.controllers.model.PaginationEnum;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class PaginationDTO {
    private final PaginationEnum pagination;
    private final Integer page;
    private final Integer customizedPage;
}
