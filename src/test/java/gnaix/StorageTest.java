package gnaix;

import gnaix.task.Deadline;
import gnaix.task.Event;
import gnaix.task.Task;
import gnaix.task.Todo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StorageTest {

    @Test
    void load_missingFile_emptyTaskListReturned(@TempDir Path tempDir)
            throws Exception {
        Storage storage = new Storage(tempDir.resolve("tasks.txt"));

        ArrayList<Task> tasks = storage.load();

        assertTrue(tasks.isEmpty());
    }

    @Test
    void saveAndLoad_todoTask_taskPreserved(@TempDir Path tempDir)
            throws Exception {
        Path filePath = tempDir.resolve("tasks.txt");
        Storage storage = new Storage(filePath);

        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("buy milk"));

        storage.save(tasks);
        ArrayList<Task> loadedTasks = storage.load();

        assertEquals(1, loadedTasks.size());
        assertEquals("buy milk", loadedTasks.get(0).getDescription());
        assertInstanceOf(Todo.class, loadedTasks.get(0));
        assertFalse(loadedTasks.get(0).isCompleted());
    }

    @Test
    void saveAndLoad_deadlineTask_datePreserved(@TempDir Path tempDir)
            throws Exception {
        Path filePath = tempDir.resolve("tasks.txt");
        Storage storage = new Storage(filePath);

        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Deadline(
                "submit assignment",
                LocalDate.of(2026, 9, 1)));

        storage.save(tasks);
        ArrayList<Task> loadedTasks = storage.load();

        assertEquals(1, loadedTasks.size());

        Deadline deadline = assertInstanceOf(
                Deadline.class,
                loadedTasks.get(0));

        assertEquals("submit assignment", deadline.getDescription());
        assertEquals(
                LocalDate.of(2026, 9, 1),
                deadline.getDoBy());
    }

    @Test
    void saveAndLoad_eventTask_timingsPreserved(@TempDir Path tempDir)
            throws Exception {
        Path filePath = tempDir.resolve("tasks.txt");
        Storage storage = new Storage(filePath);

        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Event(
                "project meeting",
                LocalDateTime.of(2026, 9, 1, 14, 0),
                LocalDateTime.of(2026, 9, 1, 16, 0)));

        storage.save(tasks);
        ArrayList<Task> loadedTasks = storage.load();

        assertEquals(1, loadedTasks.size());

        Event event = assertInstanceOf(
                Event.class,
                loadedTasks.get(0));

        assertEquals("project meeting", event.getDescription());
        assertEquals(
                LocalDateTime.of(2026, 9, 1, 14, 0),
                event.getFrom());
        assertEquals(
                LocalDateTime.of(2026, 9, 1, 16, 0),
                event.getTo());
    }

    @Test
    void saveAndLoad_completedTask_completionStatusPreserved(
            @TempDir Path tempDir) throws Exception {
        Path filePath = tempDir.resolve("tasks.txt");
        Storage storage = new Storage(filePath);

        Todo todo = new Todo("buy milk");
        todo.markAsComplete();

        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(todo);

        storage.save(tasks);
        ArrayList<Task> loadedTasks = storage.load();

        assertEquals(1, loadedTasks.size());
        assertTrue(loadedTasks.get(0).isCompleted());
    }
}