package gnaix;

import gnaix.task.Deadline;
import gnaix.task.Event;
import gnaix.task.Task;
import gnaix.task.Todo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents persistent storage for Gnaix tasks.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates storage using the specified file path.
     *
     * @param filePath Path to the task data file.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves all tasks to the storage file.
     *
     * @param tasks Tasks to persist.
     * @throws IOException If the file cannot be written.
     */
    public void save(List<Task> tasks) throws IOException {
        Path parent = filePath.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        List<String> lines = new ArrayList<>();

        for (Task task : tasks) {
            lines.add(taskToString(task));
        }

        Files.write(
                filePath,
                lines,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * Loads tasks from the storage file.
     *
     * @return Tasks loaded from the file, or an empty list if the file does not exist.
     * @throws IOException If the file cannot be read.
     */
    public ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return tasks;
        }

        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }

            try {
                tasks.add(parseTask(line));
            } catch (IllegalArgumentException e) {
                System.out.println("corrupted task data :(");
            }
        }

        return tasks;
    }

    /**
     * Parses a stored task record into a task object.
     *
     * @param line Stored task record.
     * @return Task represented by the record.
     * @throws IllegalArgumentException If the record has an invalid format.
     */
    private Task parseTask(String line) {
        String[] parts = line.split("\\|", -1);
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid task format :(");
        }

        String type = parts[0];
        if (!parts[1].equals("0") && !parts[1].equals("1")) {
            throw new IllegalArgumentException("Invalid completion status :(");
        }
        boolean completed = parts[1].equals("1");
        Task task;

        switch (type) {
            case "T":
                if (parts.length != 3) {
                    throw new IllegalArgumentException("Invalid Todo format");
                }
                task = new Todo(parts[2]);
                break;

            case "D":
                if (parts.length != 4) {
                    throw new IllegalArgumentException("Invalid Deadline format");
                }
                task = new Deadline(parts[2], LocalDate.parse(parts[3]));
                break;

            case "E":
                if (parts.length != 5) {
                    throw new IllegalArgumentException("Invalid Event format");
                }
                task = new Event(
                        parts[2],
                        LocalDateTime.parse(parts[3]),
                        LocalDateTime.parse(parts[4]));
                break;

            default:
                throw new IllegalArgumentException("Unknown task type: " + type);
        }

        if (completed) {
            task.markAsComplete();
        }
        return task;
    }

    /**
     * Converts a task into its persistent storage representation.
     *
     * @param task Task to serialize.
     * @return String representation suitable for storage.
     * @throws IllegalArgumentException If the task type is unsupported.
     */
    private String taskToString(Task task) {
        int completed = task.isCompleted() ? 1 : 0;

        if (task instanceof Todo) {
            return "T | " + completed + " | " + task.getDescription();
        }

        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return "D | " + completed + " | "
                    + task.getDescription() + " | " + deadline.getDoBy();
        }

        if (task instanceof Event) {
            Event event = (Event) task;
            return "E | " + completed + " | "
                    + task.getDescription() + " | "
                    + event.getFrom() + " | " + event.getTo();
        }

        throw new IllegalArgumentException("Unknown task type");
    }
}