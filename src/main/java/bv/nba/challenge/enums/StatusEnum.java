package bv.nba.challenge.enums;

public enum StatusEnum {

    SUCCESS("success"),
    FAILURE("failure");

    private final String status;

    StatusEnum(String status) {
        this.status = status;
    }

    public String value() {
        return status;
    }

}
