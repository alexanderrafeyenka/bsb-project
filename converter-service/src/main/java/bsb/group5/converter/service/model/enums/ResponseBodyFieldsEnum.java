package bsb.group5.converter.service.model.enums;

import lombok.Getter;

@Getter
public enum ResponseBodyFieldsEnum {
    InvalidFile("invalid file"),
    InvalidEntryLines("invalid entry lines index"),
    RepeatedApplications("repeated applications"),
    DuplicateApplications("Файл содержит дубль заявки"),
    SavedApplications("saved applications");

    private String message;

    ResponseBodyFieldsEnum(String message) {
        this.message = message;
    }
}
