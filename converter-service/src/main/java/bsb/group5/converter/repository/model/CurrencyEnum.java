package bsb.group5.converter.repository.model;

import lombok.Getter;

@Getter
public enum CurrencyEnum {
    BYN(933),
    USD(840),
    EUR(978),
    RUB(643);

    private Integer value;

    CurrencyEnum(Integer value) {
        this.value = value;
    }
}
