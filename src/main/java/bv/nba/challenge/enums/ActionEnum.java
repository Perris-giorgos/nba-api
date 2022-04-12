package bv.nba.challenge.enums;

public enum ActionEnum {

    CREATE("create comment"),
    MODIFY("modify comment"),
    DELETE("delete comment"),
    RETRIEVE_ID("retrieve by id"),
    RETRIEVE_DATE("retrieve by date");

    private final String action;

    ActionEnum(String action) {
        this.action = action;
    }

    public String value() {
        return action;
    }

}
