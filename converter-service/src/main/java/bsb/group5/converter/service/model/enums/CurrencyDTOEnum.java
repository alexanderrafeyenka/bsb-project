package bsb.group5.converter.service.model.enums;

import lombok.Getter;

@Getter
public enum CurrencyDTOEnum {
    BYN(933),
    USD(840),
    EUR(978),
    RUB(643);

    private Integer value;

    CurrencyDTOEnum(Integer value) {
        this.value = value;
    }
}
