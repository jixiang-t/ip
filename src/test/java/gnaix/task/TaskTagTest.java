package gnaix.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests tag management in Task.
 */
public class TaskTagTest {

    @Test
    public void addTag_tagStoredWithoutHash() {
        Task task = new Task("study CS2103T");

        task.addTag("#school");

        assertEquals(List.of("school"), List.copyOf(task.getTags()));
    }

    @Test
    public void addTag_mixedCase_normalisedToLowercase() {
        Task task = new Task("study CS2103T");

        task.addTag("#School");

        assertTrue(task.hasTag("#school"));
        assertTrue(task.hasTag("#SCHOOL"));
    }

    @Test
    public void addTag_duplicateTag_storedOnce() {
        Task task = new Task("study CS2103T");

        task.addTag("#school");
        task.addTag("#School");

        assertEquals(1, task.getTags().size());
    }

    @Test
    public void hasTag_missingTag_falseReturned() {
        Task task = new Task("study CS2103T");

        task.addTag("#school");

        assertFalse(task.hasTag("#urgent"));
    }

    @Test
    public void toString_taggedTask_tagsDisplayed() {
        Task task = new Task("study CS2103T");

        task.addTag("#school");
        task.addTag("#urgent");

        assertEquals(
                "[ ] study CS2103T #school #urgent",
                task.toString());
    }

    @Test
    public void toString_untaggedTask_unchanged() {
        Task task = new Task("study CS2103T");

        assertEquals(
                "[ ] study CS2103T",
                task.toString());
    }
}
