package gnaix;

import gnaix.task.Task;
import gnaix.task.TaskList;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Gnaix {
    private static final Path FILE_PATH = Path.of("data", "gnaix.txt");

    private static final DateTimeFormatter OUTPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy");

    private static final Ui ui = new Ui();
    private static final Storage storage = new Storage(FILE_PATH);
    private static TaskList tasks;

    public static void main(String[] args) {
        ui.showWelcome();

        try {
            tasks = new TaskList(storage.load());
        } catch (IOException e) {
            ui.showError("I couldn't load your tasks :(");
            tasks = new TaskList();
        }

        boolean isRunning = true;
        while (isRunning) {
            ui.showLine();
            String input = ui.readCommand();
            ParsedCommand parsed = Parser.parse(input);

            if (parsed.hasError()) {
                ui.showError(parsed.getError());
                continue;
            }

            switch (parsed.getCommand()) {
                case BYE:
                    isRunning = false;
                    break;
                case LIST:
                    ui.showTasks(tasks);
                    break;
                case TODO:
                case DEADLINE:
                case EVENT:
                    addTask(parsed.getTask());
                    break;
                case MARK:
                    markTask(parsed.getIndex());
                    break;
                case UNMARK:
                    unmarkTask(parsed.getIndex());
                    break;
                case DELETE:
                    deleteTask(parsed.getIndex());
                    break;
                case DATE:
                    listTasksOnDate(parsed.getDate());
                    break;
                default:
                    // Unreachable: error inputs are handled above.
                    break;
            }
        }

        ui.showGoodbye();
    }

    private static void addTask(Task task) {
        tasks.add(task);
        ui.showTaskAdded(task, tasks.size());
        save();
    }

    private static void markTask(int index) {
        if (!isInRange(index)) {
            ui.showError("That task number does not exist! :(");
            return;
        }
        tasks.mark(index - 1);
        ui.showTaskCompleted(tasks.get(index - 1));
        save();
    }

    private static void unmarkTask(int index) {
        if (!isInRange(index)) {
            ui.showError("That task number does not exist! :(");
            return;
        }
        tasks.unmark(index - 1);
        ui.showTaskUncompleted(tasks.get(index - 1));
        save();
    }

    private static void deleteTask(int index) {
        if (!isInRange(index)) {
            ui.showError("That task number does not exist! :(");
            return;
        }
        Task deleted = tasks.delete(index - 1);
        ui.showTaskDeleted(deleted, tasks.size());
        save();
    }

    private static void listTasksOnDate(LocalDate date) {
        ui.showMessage("Tasks occurring on " + date.format(OUTPUT_DATE_FORMAT) + ":");

        boolean found = false;
        for (Task task : tasks) {
            if (task.occursOn(date)) {
                ui.showMessage(task.toString());
                found = true;
            }
        }

        if (!found) {
            ui.showMessage("No deadlines or events found on that date.");
        }
    }

    private static boolean isInRange(int index) {
        return index >= 1 && index <= tasks.size();
    }

    private static void save() {
        try {
            storage.save(tasks.getTasks());
        } catch (IOException e) {
            ui.showError("I couldn't save your tasks! :(");
        }
    }
}