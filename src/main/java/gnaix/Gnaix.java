package gnaix;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import gnaix.task.Task;
import gnaix.task.TaskList;

/**
 * Runs the Gnaix task management application.
 */
public class Gnaix {
    private static final Path FILE_PATH = Path.of("data", "gnaix.txt");

    private static final DateTimeFormatter OUTPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy");

    private final Ui ui;
    private final Storage storage;
    private TaskList tasks;

    /**
     * Creates a Gnaix application using the default storage file.
     */
    public Gnaix() {
        this.ui = new Ui();
        this.storage = new Storage(FILE_PATH);

        try {
            this.tasks = new TaskList(storage.load());
        } catch (IOException e) {
            this.tasks = new TaskList();
        }
    }

    /**
     * Starts the text-based Gnaix application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        Gnaix gnaix = new Gnaix();
        gnaix.run();
    }

    /**
     * Runs the text-based Gnaix application.
     */
    private void run() {
        ui.showWelcome();

        boolean isRunning = true;

        while (isRunning) {
            ui.showLine();
            String input = ui.readCommand();
            ParsedCommand parsed = Parser.parse(input);

            if (parsed.hasError()) {
                ui.showError(parsed.getError());
                continue;
            }

            if (parsed.getCommand() == Command.BYE) {
                isRunning = false;
                continue;
            }

            executeCommand(parsed);
        }

        ui.showGoodbye();
    }

    /**
     * Executes a parsed command.
     *
     * @param parsed Parsed command to execute.
     */
    private void executeCommand(ParsedCommand parsed) {
        switch (parsed.getCommand()) {
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
            case BYE:
            default:
                break;
        }
    }

    /**
     * Adds a task to the task list and saves the updated list.
     *
     * @param task Task to add.
     */
    private void addTask(Task task) {
        tasks.add(task);
        ui.showTaskAdded(task, tasks.size());
        save();
    }

    /**
     * Marks the specified task as complete.
     *
     * @param index One-based task number entered by the user.
     */
    private void markTask(int index) {
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
    private void unmarkTask(int index) {
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
    private void deleteTask(int index) {
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
    private void listTasksOnDate(LocalDate date) {
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
    private boolean isInRange(int index) {
        return index >= 1 && index <= tasks.size();
    }

    /**
     * Saves the current task list to persistent storage.
     */
    private void save() {
        try {
            storage.save(tasks.getTasks());
        } catch (IOException e) {
            ui.showError("I couldn't save your tasks! :(");
        }
    }

    /**
     * Displays tasks whose descriptions contain the specified keyword.
     *
     * @param keyword Keyword to search for.
     */
    private void findTasks(String keyword) {
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

    /**
     * Processes a user command and returns the response for the GUI.
     *
     * @param input User command.
     * @return Response generated by Gnaix.
     */
    public String getResponse(String input) {
        ParsedCommand parsed = Parser.parse(input);

        if (parsed.hasError()) {
            return parsed.getError();
        }

        switch (parsed.getCommand()) {
            case BYE:
                return "Bye. Hope to see you again soon!";
            case LIST:
                return getTaskListResponse();
            case TODO:
            case DEADLINE:
            case EVENT:
                return addTaskAndGetResponse(parsed.getTask());
            case MARK:
                return markTaskAndGetResponse(parsed.getIndex());
            case UNMARK:
                return unmarkTaskAndGetResponse(parsed.getIndex());
            case DELETE:
                return deleteTaskAndGetResponse(parsed.getIndex());
            case DATE:
                return getDateResponse(parsed.getDate());
            case FIND:
                return getFindResponse(parsed.getKeyword());
            default:
                return "That's not a valid command! :(";
        }
    }

    /**
     * Returns a formatted representation of the current task list.
     *
     * @return Formatted task list.
     */
    private String getTaskListResponse() {
        StringBuilder response = new StringBuilder("Here are the tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            response.append(System.lineSeparator())
                    .append(i + 1)
                    .append(". ")
                    .append(tasks.get(i));
        }

        return response.toString();
    }

    /**
     * Adds a task and returns the corresponding response.
     *
     * @param task Task to add.
     * @return Response describing the added task.
     */
    private String addTaskAndGetResponse(Task task) {
        tasks.add(task);
        save();

        return "Got it. I've added this task:"
                + System.lineSeparator()
                + "  " + task
                + System.lineSeparator()
                + "Now you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Marks a task as complete and returns the corresponding response.
     *
     * @param index One-based task number.
     * @return Response describing the result.
     */
    private String markTaskAndGetResponse(int index) {
        if (!isInRange(index)) {
            return "That task number does not exist! :(";
        }

        tasks.mark(index - 1);
        Task task = tasks.get(index - 1);
        save();

        return "Nice! I've marked this task as done:"
                + System.lineSeparator()
                + "  " + task;
    }

    /**
     * Marks a task as incomplete and returns the corresponding response.
     *
     * @param index One-based task number.
     * @return Response describing the result.
     */
    private String unmarkTaskAndGetResponse(int index) {
        if (!isInRange(index)) {
            return "That task number does not exist! :(";
        }

        tasks.unmark(index - 1);
        Task task = tasks.get(index - 1);
        save();

        return "OK, I've marked this task as not done yet:"
                + System.lineSeparator()
                + "  " + task;
    }

    /**
     * Deletes a task and returns the corresponding response.
     *
     * @param index One-based task number.
     * @return Response describing the deleted task.
     */
    private String deleteTaskAndGetResponse(int index) {
        if (!isInRange(index)) {
            return "That task number does not exist! :(";
        }

        Task deleted = tasks.delete(index - 1);
        save();

        return "Noted. I've removed this task:"
                + System.lineSeparator()
                + "  " + deleted
                + System.lineSeparator()
                + "Now you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Returns tasks whose descriptions contain the specified keyword.
     *
     * @param keyword Keyword to search for.
     * @return Formatted matching tasks.
     */
    private String getFindResponse(String keyword) {
        StringBuilder response =
                new StringBuilder("Here are the matching tasks in your list:");

        boolean found = false;

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);

            if (task.getDescription().toLowerCase()
                    .contains(keyword.toLowerCase())) {
                response.append(System.lineSeparator())
                        .append(i + 1)
                        .append(". ")
                        .append(task);

                found = true;
            }
        }

        if (!found) {
            response.append(System.lineSeparator())
                    .append("No matching tasks found :(");
        }

        return response.toString();
    }

    /**
     * Returns tasks occurring on the specified date.
     *
     * @param date Date to search.
     * @return Formatted tasks occurring on the date.
     */
    private String getDateResponse(LocalDate date) {
        StringBuilder response = new StringBuilder(
                "Tasks occurring on " + date.format(OUTPUT_DATE_FORMAT) + ":");

        boolean found = false;

        for (Task task : tasks) {
            if (task.occursOn(date)) {
                response.append(System.lineSeparator())
                        .append(task);

                found = true;
            }
        }

        if (!found) {
            response.append(System.lineSeparator())
                    .append("No deadlines or events found on that date.");
        }

        return response.toString();
    }
}
