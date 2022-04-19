package bsb.group5.converter.service.model;

import bsb.group5.converter.service.model.enums.StatusDTOEnum;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class UpdateStatusApplicationDTO {
    private final UUID applicationConvId;
    private final StatusDTOEnum status;
}
