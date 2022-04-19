package bsb.group5.converter.repository.model;

public enum StatusEnum {
    New("New"),
    InProgress("In Progress"),
    Done("Done"),
    Rejected("Rejected");

    public String value;

    StatusEnum(String value) {
        this.value = value;
    }
}
