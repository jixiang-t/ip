import java.time.LocalDate;

public class ParsedCommand {
    private final Command command;
    private final Task task;
    private final int index;
    private final LocalDate date;
    private final String error;

    private ParsedCommand(Command command, Task task, int index,
                          LocalDate date, String error) {
        this.command = command;
        this.task = task;
        this.index = index;
        this.date = date;
        this.error = error;
    }

    /** For BYE, LIST */
    public static ParsedCommand of(Command command) {
        return new ParsedCommand(command, null, -1, null, null);
    }

    /** For TODO, DEADLINE, EVENT */
    public static ParsedCommand forTask(Command command, Task task) {
        return new ParsedCommand(command, task, -1, null, null);
    }

    /** For MARK, UNMARK, DELETE */
    public static ParsedCommand forIndex(Command command, int index) {
        return new ParsedCommand(command, null, index, null, null);
    }

    /** For DATE **/
    public static ParsedCommand forDate(LocalDate date) {
        return new ParsedCommand(Command.DATE, null, -1, date, null);
    }

    /** For input the Parser could not understand; carries the message to show. */
    public static ParsedCommand error(String message) {
        return new ParsedCommand(Command.UNKNOWN, null, -1, null, message);
    }

    public boolean hasError() {
        return error != null;
    }

    public String getError() {
        return error;
    }

    public Command getCommand() {
        return command;
    }

    public Task getTask() {
        return task;
    }

    public int getIndex() {
        return index;
    }

    public LocalDate getDate() {
        return date;
    }
}