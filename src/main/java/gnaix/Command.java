package gnaix;

public enum Command {
    BYE,
    LIST,
    MARK,
    UNMARK,
    DELETE,
    TODO,
    DEADLINE,
    EVENT,
    DATE,
    FIND,
    UNKNOWN;

    public static Command fromString(String input) {
        try {
            return Command.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
