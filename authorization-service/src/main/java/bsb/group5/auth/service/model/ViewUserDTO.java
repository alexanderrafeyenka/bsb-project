package bsb.group5.auth.service.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode
public class ViewUserDTO {
    private final Long id;
    private final StatusDTOEnum status;
}
