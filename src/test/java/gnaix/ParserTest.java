package gnaix;

import gnaix.task.Deadline;
import gnaix.task.Event;
import gnaix.task.Task;
import gnaix.task.Todo;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParserTest {

    @Test
    void parse_validTodoCommand_todoCreated() {
        ParsedCommand result = Parser.parse("todo buy milk");

        assertFalse(result.hasError());
        assertEquals(Command.TODO, result.getCommand());

        Task task = result.getTask();
        assertTrue(task instanceof Todo);
        assertEquals("buy milk", task.getDescription());
    }

    @Test
    void parse_validDeadlineCommand_deadlineCreated() {
        ParsedCommand result = Parser.parse(
                "deadline return book /by 2026-09-01");

        assertFalse(result.hasError());
        assertEquals(Command.DEADLINE, result.getCommand());

        Task task = result.getTask();
        assertTrue(task instanceof Deadline);

        Deadline deadline = (Deadline) task;
        assertEquals("return book", deadline.getDescription());
        assertEquals(LocalDate.of(2026, 9, 1), deadline.getDoBy());
    }

    @Test
    void parse_validEventCommand_eventCreated() {
        ParsedCommand result = Parser.parse(
                "event project meeting /from 2026-09-01 1400 "
                        + "/to 2026-09-01 1600");

        assertFalse(result.hasError());
        assertEquals(Command.EVENT, result.getCommand());

        Task task = result.getTask();
        assertTrue(task instanceof Event);

        Event event = (Event) task;
        assertEquals("project meeting", event.getDescription());
        assertEquals(
                LocalDateTime.of(2026, 9, 1, 14, 0),
                event.getFrom());
        assertEquals(
                LocalDateTime.of(2026, 9, 1, 16, 0),
                event.getTo());
    }

    @Test
    void parse_validDateCommand_dateParsed() {
        ParsedCommand result = Parser.parse("date 2026-09-01");

        assertFalse(result.hasError());
        assertEquals(Command.DATE, result.getCommand());
        assertEquals(
                LocalDate.of(2026, 9, 1),
                result.getDate());
    }

    @Test
    void parse_validListCommand_listReturned() {
        ParsedCommand result = Parser.parse("list");

        assertFalse(result.hasError());
        assertEquals(Command.LIST, result.getCommand());
        assertNull(result.getTask());
    }

    @Test
    void parse_validIndexCommand_indexParsed() {
        ParsedCommand result = Parser.parse("mark 3");

        assertFalse(result.hasError());
        assertEquals(Command.MARK, result.getCommand());
        assertEquals(3, result.getIndex());
    }

    @Test
    void parse_emptyCommand_errorReturned() {
        ParsedCommand result = Parser.parse("");

        assertTrue(result.hasError());
        assertEquals(Command.UNKNOWN, result.getCommand());
        assertEquals(
                "Please enter a command! :(",
                result.getError());
    }

    @Test
    void parse_unknownCommand_errorReturned() {
        ParsedCommand result = Parser.parse("hello");

        assertTrue(result.hasError());
        assertEquals(Command.UNKNOWN, result.getCommand());
        assertEquals(
                "That's not a valid command! :(",
                result.getError());
        assertNull(result.getTask());
    }

    @Test
    void parseTodo_missingDescription_errorReturned() {
        ParsedCommand result = Parser.parse("todo");

        assertTrue(result.hasError());
        assertEquals(
                "NO DESCRIPTION GIVEN! :(",
                result.getError());
    }

    @Test
    void parseDeadline_missingByDate_errorReturned() {
        ParsedCommand result = Parser.parse("deadline return book");

        assertTrue(result.hasError());
        assertEquals(
                "A deadline needs a description and a /by date! :(",
                result.getError());
    }

    @Test
    void parseDeadline_invalidDate_errorReturned() {
        ParsedCommand result = Parser.parse(
                "deadline return book /by 2026-99-99");

        assertTrue(result.hasError());
        assertEquals(
                "Please enter the deadline as yyyy-MM-dd! :(",
                result.getError());
    }

    @Test
    void parseEvent_missingTimings_errorReturned() {
        ParsedCommand result = Parser.parse(
                "event project meeting");

        assertTrue(result.hasError());
        assertEquals(
                "Not enough info given! :(",
                result.getError());
    }

    @Test
    void parseEvent_missingToTime_errorReturned() {
        ParsedCommand result = Parser.parse(
                "event project meeting /from 2026-09-01 1400");

        assertTrue(result.hasError());
        assertEquals(
                "An event needs a description and timings! :(",
                result.getError());
    }

    @Test
    void parseEvent_invalidTime_errorReturned() {
        ParsedCommand result = Parser.parse(
                "event project meeting /from 2026-99-99 1400 "
                        + "/to 2026-09-01 1600");

        assertTrue(result.hasError());
        assertEquals(
                "Please enter event times as yyyy-MM-dd HHmm! :(",
                result.getError());
    }

    @Test
    void parseDate_invalidDate_errorReturned() {
        ParsedCommand result = Parser.parse("date 2026-99-99");

        assertTrue(result.hasError());
        assertEquals(
                "Please enter the date as yyyy-MM-dd! :(",
                result.getError());
    }

    @Test
    void parseIndex_nonNumericValue_errorReturned() {
        ParsedCommand result = Parser.parse("mark abc");

        assertTrue(result.hasError());
        assertEquals(
                "That task number is not a number! :(",
                result.getError());
    }

    @Test
    void parseIndex_missingValue_errorReturned() {
        ParsedCommand result = Parser.parse("mark");

        assertTrue(result.hasError());
        assertEquals(
                "That task number is not a number! :(",
                result.getError());
    }
}