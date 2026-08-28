package gnaix;

import gnaix.task.Task;
import gnaix.task.TaskList;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Runs the Gnaix task management application.
 */
public class Gnaix {
    private static final Path FILE_PATH = Path.of("data", "gnaix.txt");

    private static final DateTimeFormatter OUTPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy");

    private static final Ui ui = new Ui();
    private static final Storage storage = new Storage(FILE_PATH);
    private static TaskList tasks;

    /**
     * Starts the Gnaix application.
     *
     * @param args Command-line arguments.
     */
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
                case FIND:
                    findTasks(parsed.getKeyword());
                    break;
                default:
                    // Unreachable: error inputs are handled above.
                    break;
            }
        }

        ui.showGoodbye();
    }

    /**
     * Adds a task to the task list and saves the updated list.
     *
     * @param task Task to add.
     */
    private static void addTask(Task task) {
        tasks.add(task);
        ui.showTaskAdded(task, tasks.size());
        save();
    }

    /**
     * Marks the specified task as complete.
     *
     * @param index One-based task number entered by the user.
     */
    private static void markTask(int index) {
        if (!isInRange(index)) {
            ui.showError("That task number does not exist! :(");
            return;
        }
        tasks.mark(index - 1);
        ui.showTaskCompleted(tasks.get(index - 1));
        save();
    }

    /**
     * Marks the specified task as incomplete.
     *
     * @param index One-based task number entered by the user.
     */
    private static void unmarkTask(int index) {
        if (!isInRange(index)) {
            ui.showError("That task number does not exist! :(");
            return;
        }
        tasks.unmark(index - 1);
        ui.showTaskUncompleted(tasks.get(index - 1));
        save();
    }

    /**
     * Deletes the specified task from the task list.
     *
     * @param index One-based task number entered by the user.
     */
    private static void deleteTask(int index) {
        if (!isInRange(index)) {
            ui.showError("That task number does not exist! :(");
            return;
        }
        Task deleted = tasks.delete(index - 1);
        ui.showTaskDeleted(deleted, tasks.size());
        save();
    }

    /**
     * Displays tasks occurring on the specified date.
     *
     * @param date Date for which tasks should be displayed.
     */
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

    /**
     * Returns whether a one-based task index refers to an existing task.
     *
     * @param index One-based task number.
     * @return True if the index is within the current task list.
     */
    private static boolean isInRange(int index) {
        return index >= 1 && index <= tasks.size();
    }

    /**
     * Saves the current task list to persistent storage.
     */
    private static void save() {
        try {
            storage.save(tasks.getTasks());
        } catch (IOException e) {
            ui.showError("I couldn't save your tasks! :(");
        }
    }

    private static void findTasks(String keyword) {
        ui.showMessage("Here are the matching tasks in your list:");

        boolean found = false;

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);

            if (task.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                ui.showMessage((i + 1) + ". " + task);
                found = true;
            }
        }

        if (!found) {
            ui.showMessage("No matching tasks found :(");
        }
    }
}