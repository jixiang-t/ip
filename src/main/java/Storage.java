import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path filePath;

    public Storage(Path filePath) {
        this.filePath = filePath;
    }

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

    public ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return tasks;
        }

        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

        for (String line : lines) {
            if (!line.isBlank()) {
                tasks.add(parseTask(line));
            }
        }

        return tasks;
    }

    private Task parseTask(String line) {
        String[] parts = line.split("\\|", -1);
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        String type = parts[0];
        boolean completed = parts[1].equals("1");
        Task task;

        switch (type) {
            case "T":
                task = new Todo(parts[2]);
                break;

            case "D":
                task = new Deadline(parts[2], parts[3]);
                break;

            case "E":
                task = new Event(parts[2], parts[3], parts[4]);
                break;

            default:
                throw new IllegalArgumentException("Unknown task type: " + type);
        }

        if (completed) {
            task.markAsComplete();
        }
        return task;
    }

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