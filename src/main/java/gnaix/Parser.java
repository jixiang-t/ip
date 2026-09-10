package gnaix;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gnaix.task.Deadline;
import gnaix.task.Event;
import gnaix.task.Task;
import gnaix.task.Todo;

/**
 * Parses user input into structured commands.
 */
public class Parser {
    private static final DateTimeFormatter INPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter INPUT_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    private static final Pattern TRAILING_TAG_PATTERN =
            Pattern.compile("\\s+(#[A-Za-z0-9_-]+)$");

    private static final Pattern TAG_PATTERN =
            Pattern.compile("#[A-Za-z0-9_-]+");

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
            case TAG:
                return parseTagSearch(args);
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
     * @param args User-supplied todo description and optional tags.
     * @return Parsed command containing the todo task or an error.
     */
    private static ParsedCommand parseTodo(String args) {
        TaggedText taggedText = extractTrailingTags(args);

        if (taggedText.text().isEmpty()) {
            return ParsedCommand.error("NO DESCRIPTION GIVEN! :(");
        }

        Todo todo = new Todo(taggedText.text());
        addTags(todo, taggedText.tags());

        return ParsedCommand.forTask(Command.TODO, todo);
    }

    /**
     * Parses a deadline task from the supplied arguments.
     *
     * @param args User-supplied deadline description, date, and optional tags.
     * @return Parsed command containing the deadline task or an error.
     */
    private static ParsedCommand parseDeadline(String args) {
        TaggedText taggedText = extractTrailingTags(args);
        String[] segments = taggedText.text().split(" /by ", 2);

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
            Deadline deadline = new Deadline(info, doBy);
            addTags(deadline, taggedText.tags());

            return ParsedCommand.forTask(Command.DEADLINE, deadline);
        } catch (DateTimeParseException e) {
            return ParsedCommand.error(
                    "Please enter the deadline as yyyy-MM-dd! :(");
        }
    }

    /**
     * Parses an event task from the supplied arguments.
     *
     * @param args User-supplied event description, timings, and optional tags.
     * @return Parsed command containing the event task or an error.
     */
    private static ParsedCommand parseEvent(String args) {
        TaggedText taggedText = extractTrailingTags(args);
        String[] parts = taggedText.text().split(" /from ", 2);

        if (parts.length < 2) {
            return ParsedCommand.error("Not enough info given! :(");
        }

        String info = parts[0].trim();
        String[] times = parts[1].split(" /to ", 2);

        if (info.isEmpty() || times.length < 2) {
            return ParsedCommand.error(
                    "An event needs a description and timings! :(");
        }

        String from = times[0].trim();
        String to = times[1].trim();

        if (from.isEmpty() || to.isEmpty()) {
            return ParsedCommand.error(
                    "An event needs a /from time, and /to time! :(");
        }

        try {
            LocalDateTime fromDateTime =
                    LocalDateTime.parse(from, INPUT_DATE_TIME_FORMAT);
            LocalDateTime toDateTime =
                    LocalDateTime.parse(to, INPUT_DATE_TIME_FORMAT);

            Event event = new Event(info, fromDateTime, toDateTime);
            addTags(event, taggedText.tags());

            return ParsedCommand.forTask(Command.EVENT, event);
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
            return ParsedCommand.error(
                    "Please provide a date in yyyy-MM-dd format! :(");
        }

        try {
            return ParsedCommand.forDate(
                    LocalDate.parse(args, INPUT_DATE_FORMAT));
        } catch (DateTimeParseException e) {
            return ParsedCommand.error(
                    "Please enter the date as yyyy-MM-dd! :(");
        }
    }

    /**
     * Parses a keyword used to search task descriptions.
     *
     * @param args User-supplied search keyword.
     * @return Parsed command containing the keyword or an error.
     */
    private static ParsedCommand parseFind(String args) {
        if (args.isEmpty()) {
            return ParsedCommand.error(
                    "Please provide a keyword to search for! :(");
        }

        return ParsedCommand.forKeyword(args);
    }

    /**
     * Parses one or more tags used to search tasks.
     *
     * @param args User-supplied tags.
     * @return Parsed command containing the tags or an error.
     */
    private static ParsedCommand parseTagSearch(String args) {
        if (args.isEmpty()) {
            return ParsedCommand.error(
                    "Please provide at least one tag to search for! :(");
        }

        String[] tokens = args.split("\\s+");
        Set<String> tags = new LinkedHashSet<>();

        for (String token : tokens) {
            if (!TAG_PATTERN.matcher(token).matches()) {
                return ParsedCommand.error(
                        "Tags must start with # and contain only "
                                + "letters, digits, _ or -! :(");
            }

            tags.add(normaliseTag(token));
        }

        return ParsedCommand.forTags(tags);
    }

    /**
     * Extracts valid tags from the end of task arguments.
     *
     * @param args Task arguments that may end with tags.
     * @return Task text and extracted tags.
     */
    private static TaggedText extractTrailingTags(String args) {
        String remaining = args.trim();
        Set<String> tags = new LinkedHashSet<>();

        while (!remaining.isEmpty()) {
            Matcher matcher = TRAILING_TAG_PATTERN.matcher(remaining);

            if (!matcher.find()) {
                break;
            }

            tags.add(normaliseTag(matcher.group(1)));
            remaining = remaining.substring(0, matcher.start()).trim();
        }

        return new TaggedText(remaining, tags);
    }

    /**
     * Adds all parsed tags to a task.
     *
     * @param task Task receiving the tags.
     * @param tags Tags to add.
     */
    private static void addTags(Task task, Set<String> tags) {
        tags.forEach(task::addTag);
    }

    /**
     * Normalises a tag for storage and comparison.
     *
     * @param tag Tag including its leading '#'.
     * @return Lowercase tag without '#'.
     */
    private static String normaliseTag(String tag) {
        return tag.substring(1).toLowerCase(Locale.ROOT);
    }

    /**
     * Holds task text together with tags extracted from its end.
     *
     * @param text Task text with trailing tags removed.
     * @param tags Extracted tags.
     */
    private record TaggedText(String text, Set<String> tags) {
    }
}
