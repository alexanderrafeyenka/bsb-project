package bsb.group5.converter.service.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class UpdateNameLegalApplicationDTO {
    private final String nameLegal;
    private UUID id;
}
