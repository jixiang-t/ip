package gnaix.task;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class DeadlineTest {

    @Test
    void occursOn_matchingDate_trueReturned() {
        Deadline deadline = new Deadline(
                "submit assignment",
                LocalDate.of(2026, 9, 1));

        assertTrue(deadline.occursOn(LocalDate.of(2026, 9, 1)));
    }

    @Test
    void occursOn_differentDate_falseReturned() {
        Deadline deadline = new Deadline(
                "submit assignment",
                LocalDate.of(2026, 9, 1));

        assertFalse(deadline.occursOn(LocalDate.of(2026, 9, 2)));
    }
}
