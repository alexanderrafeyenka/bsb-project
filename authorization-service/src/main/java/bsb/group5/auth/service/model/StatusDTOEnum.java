package bsb.group5.auth.service.model;

import lombok.Getter;

@Getter
public enum StatusDTOEnum {
    ACTIVE("Active"),
    DISABLED("Disabled");

    private String value;

    StatusDTOEnum(String value) {
        this.value = value;
    }

    public static StatusDTOEnum get(boolean isActivated) {
        if (isActivated) {
            return StatusDTOEnum.ACTIVE;
        } else {
            return StatusDTOEnum.DISABLED;
        }
    }
}
