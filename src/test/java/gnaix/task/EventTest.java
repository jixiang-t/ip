package gnaix.task;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class EventTest {

    @Test
    void occursOn_dateWithinEventRange_trueReturned() {
        Event event = new Event(
                "project meeting",
                LocalDateTime.of(2026, 9, 1, 14, 0),
                LocalDateTime.of(2026, 9, 3, 16, 0));

        assertTrue(event.occursOn(LocalDate.of(2026, 9, 2)));
    }

    @Test
    void occursOn_dateOutsideEventRange_falseReturned() {
        Event event = new Event(
                "project meeting",
                LocalDateTime.of(2026, 9, 1, 14, 0),
                LocalDateTime.of(2026, 9, 3, 16, 0));

        assertFalse(event.occursOn(LocalDate.of(2026, 9, 4)));
    }
}
