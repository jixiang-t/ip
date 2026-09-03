package gnaix;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import gnaix.task.Deadline;
import gnaix.task.Event;
import gnaix.task.Todo;

/**
 * Parses user input into structured commands.
 */
public class Parser {
    private static final DateTimeFormatter INPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter INPUT_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /**
     * Parses a full user command into a structured result.
     *
     * @param fullCommand Complete command entered by the user.
     * @return Parsed representation of the command or an error.
     */
    public static ParsedCommand parse(String fullCommand) {
        String trimmed = fullCommand.trim();
        if (trimmed.isEmpty()) {
            return ParsedCommand.error("Please enter a command! :(");
        }

        String[] parts = trimmed.split("\\s+", 2);
        Command command = Command.fromString(parts[0]);
        String args = parts.length > 1 ? parts[1].trim() : "";

        switch (command) {
            case BYE:
            case LIST:
                return ParsedCommand.of(command);
            case MARK:
            case UNMARK:
            case DELETE:
                return parseIndex(command, args);
            case TODO:
                return parseTodo(args);
            case DEADLINE:
                return parseDeadline(args);
            case EVENT:
                return parseEvent(args);
            case DATE:
                return parseDate(args);
            case FIND:
                return parseFind(args);
            default:
                return ParsedCommand.error("That's not a valid command! :(");
        }
    }

    /**
     * Parses a task index from the supplied command arguments.
     *
     * @param command Command containing the index.
     * @param args User-supplied index arguments.
     * @return Parsed command containing the task index or an error.
     */
    private static ParsedCommand parseIndex(Command command, String args) {
        try {
            return ParsedCommand.forIndex(command, Integer.parseInt(args.trim()));
        } catch (NumberFormatException e) {
            return ParsedCommand.error("That task number is not a number! :(");
        }
    }

    /**
     * Parses a todo task from the supplied arguments.
     *
     * @param args User-supplied todo description.
     * @return Parsed command containing the todo task or an error.
     */
    private static ParsedCommand parseTodo(String args) {
        if (args.isEmpty()) {
            return ParsedCommand.error("NO DESCRIPTION GIVEN! :(");
        }
        return ParsedCommand.forTask(Command.TODO, new Todo(args));
    }

    /**
     * Parses a deadline task from the supplied arguments.
     *
     * @param args User-supplied deadline description and date.
     * @return Parsed command containing the deadline task or an error.
     */
    private static ParsedCommand parseDeadline(String args) {
        String[] segments = args.split(" /by ", 2);
        if (segments.length < 2) {
            return ParsedCommand.error(
                    "A deadline needs a description and a /by date! :(");
        }

        String info = segments[0].trim();
        String by = segments[1].trim();
        if (info.isEmpty() || by.isEmpty()) {
            return ParsedCommand.error(
                    "A deadline needs a description and a /by date! :(");
        }

        try {
            LocalDate doBy = LocalDate.parse(by, INPUT_DATE_FORMAT);
            return ParsedCommand.forTask(
                    Command.DEADLINE,
                    new Deadline(info, doBy));
        } catch (DateTimeParseException e) {
            return ParsedCommand.error("Please enter the deadline as yyyy-MM-dd! :(");
        }
    }

    /**
     * Parses an event task from the supplied arguments.
     *
     * @param args User-supplied event description and timing information.
     * @return Parsed command containing the event task or an error.
     */
    private static ParsedCommand parseEvent(String args) {
        String[] parts = args.split(" /from ", 2);
        if (parts.length < 2) {
            return ParsedCommand.error("Not enough info given! :(");
        }

        String info = parts[0].trim();
        String[] times = parts[1].split(" /to ", 2);
        if (info.isEmpty() || times.length < 2) {
            return ParsedCommand.error("An event needs a description and timings! :(");
        }

        String from = times[0].trim();
        String to = times[1].trim();
        if (from.isEmpty() || to.isEmpty()) {
            return ParsedCommand.error("An event needs a /from time, and /to time! :(");
        }

        try {
            LocalDateTime fromDateTime =
                    LocalDateTime.parse(from, INPUT_DATE_TIME_FORMAT);
            LocalDateTime toDateTime =
                    LocalDateTime.parse(to, INPUT_DATE_TIME_FORMAT);
            return ParsedCommand.forTask(Command.EVENT,
                    new Event(info, fromDateTime, toDateTime));
        } catch (DateTimeParseException e) {
            return ParsedCommand.error(
                    "Please enter event times as yyyy-MM-dd HHmm! :(");
        }
    }

    /**
     * Parses a date from the supplied arguments.
     *
     * @param args User-supplied date.
     * @return Parsed command containing the date or an error.
     */
    private static ParsedCommand parseDate(String args) {
        if (args.isEmpty()) {
            return ParsedCommand.error("Please provide a date in yyyy-MM-dd format! :(");
        }
        try {
            return ParsedCommand.forDate(LocalDate.parse(args, INPUT_DATE_FORMAT));
        } catch (DateTimeParseException e) {
            return ParsedCommand.error("Please enter the date as yyyy-MM-dd! :(");
        }
    }

    private static ParsedCommand parseFind(String args) {
        if (args.isEmpty()) {
            return ParsedCommand.error(
                    "Please provide a keyword to search for! :(");
        }

        return ParsedCommand.forKeyword(args);
    }
}
