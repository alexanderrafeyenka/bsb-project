package bsb.group5.converter.service.model;

import bsb.group5.converter.service.model.enums.StatusDTOEnum;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ViewUpdateStatusAppDTO {
    private final String username;
    private final StatusDTOEnum status;
}
