package bsb.group5.converter.service.model.enums;

public enum StatusDTOEnum {
    New("New"),
    InProgress("In Progress"),
    Done("Done"),
    Rejected("Rejected");

    public String value;

    StatusDTOEnum(String value) {
        this.value = value;
    }
}
