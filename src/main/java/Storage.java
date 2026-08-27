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