package bsb.group5.company.service.model;

import lombok.Getter;

@Getter
public enum TypeLegalDTOEnum {
    Resident("Resident"),
    NoResident("NoResident");

    private String typeName;

    TypeLegalDTOEnum(String type) {
        this.typeName = type;
    }
}
