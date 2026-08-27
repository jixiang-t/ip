import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Gnaix {
    private static final DateTimeFormatter INPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter INPUT_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    private static final DateTimeFormatter OUTPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy");

    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        Storage storage = new Storage(Path.of("data", "gnaix.txt"));
        TaskList tasks;
        try {
            tasks = new TaskList(storage.load());
        } catch (IOException e) {
            ui.showError("I couldn't load your tasks :(");
            tasks = new TaskList();
        }
        boolean isRunning = true;

        while (isRunning) {
            ui.showLine();
            String cmd = ui.readCommand();

            if (cmd.isEmpty()) {
                ui.showError("Please enter a command! :(");
                continue;
            }
            String[] commandParts = cmd.split("\\s+", 2);
            Command command = Command.fromString(commandParts[0]);
            String arguments = commandParts.length > 1 ? commandParts[1] : "";

            switch (command) {
                case BYE:
                    isRunning = false;
                    break;

                case LIST:
                    listTasks(tasks, ui);
                    break;

                case MARK:
                    try {
                        int taskNumber = Integer.parseInt(arguments);
                        if (taskNumber < 1 || taskNumber > tasks.size()) {
                            ui.showError("That task number does not exist! :(");
                        } else {
                            completeTask(tasks, ui, taskNumber);
                            saveTasks(storage, tasks);
                        }
                    } catch (NumberFormatException e) {
                        ui.showError("That task number is not a number! :(");
                    }
                    break;

                case UNMARK:
                    try {
                        int taskNumber = Integer.parseInt(arguments);

                        if (taskNumber < 1 || taskNumber > tasks.size()) {
                            ui.showError("That task number does not exist! :(");
                        } else {
                            uncompleteTask(tasks, ui, taskNumber);
                            saveTasks(storage, tasks);
                        }
                    } catch (NumberFormatException e) {
                        ui.showError("That task number is not a number! :(");
                    }
                    break;

                case DELETE:
                    try {
                        int taskNumber = Integer.parseInt(arguments);

                        if (taskNumber < 1 || taskNumber > tasks.size()) {
                            ui.showError("That task number does not exist! :(");
                        } else {
                            deleteTask(tasks, ui, taskNumber);
                            saveTasks(storage, tasks);
                        }
                    } catch (NumberFormatException e) {
                        ui.showError("That task number is not a number! :(");
                    }
                    break;

                case TODO:
                    addToDo(tasks, ui, arguments);
                    saveTasks(storage, tasks);
                    break;

                case DEADLINE:
                    addDeadline(tasks, ui, arguments);
                    saveTasks(storage, tasks);
                    break;

                case EVENT:
                    addEvent(tasks, ui, arguments);
                    saveTasks(storage, tasks);
                    break;

                case DATE:
                    listTasksOnDate(tasks, ui, arguments);
                    break;

                case UNKNOWN:
                    ui.showError("That's not a valid command! :(");
                    break;
            }
        }

        ui.showGoodbye();
    }

    private static void addToDo(TaskList tasks, Ui ui, String description) {
        description = description.trim();

        if (description.isEmpty()) {
            ui.showError("NO DESCRIPTION GIVEN! :(");
            return;
        }

        Task task = new Todo(description);
        tasks.add(task);
        ui.showTaskAdded(task, tasks.size());
    }

    private static void addDeadline(TaskList tasks, Ui ui, String description) {
        String[] parts = description.split(" /by ", 2);

        if (parts.length < 2) {
            ui.showError("A deadline needs a description and a /by date! :(");
            return;
        }

        String info = parts[0].trim();
        String by = parts[1].trim();

        if (info.isEmpty() || by.isEmpty()) {
            ui.showError("A deadline needs a description and a /by date! :(");
            return;
        }

        try {
            LocalDate deadlineDate =
                    LocalDate.parse(by, INPUT_DATE_FORMAT);

            Task task = new Deadline(info, deadlineDate);
            tasks.add(task);
            ui.showTaskAdded(task, tasks.size());
        } catch (DateTimeParseException e) {
            ui.showError("Please enter the deadline as yyyy-MM-dd! :(");
        }
    }

    private static void addEvent(TaskList tasks, Ui ui, String description) {
        String[] parts = description.split(" /from ", 2);

        if (parts.length < 2) {
            ui.showError("Not enough info given! :(");
            return;
        }

        String info = parts[0].trim();
        String[] times = parts[1].split(" /to ", 2);

        if (info.isEmpty() || times.length < 2) {
            ui.showError("An event needs a description and timings! :(");
            return;
        }

        String from = times[0].trim();
        String to = times[1].trim();

        if (from.isEmpty() || to.isEmpty()) {
            ui.showError("An event needs a /from time, and /to time! :(");
            return;
        }

        try {
            LocalDateTime fromDateTime =
                    LocalDateTime.parse(from, INPUT_DATE_TIME_FORMAT);
            LocalDateTime toDateTime =
                    LocalDateTime.parse(to, INPUT_DATE_TIME_FORMAT);

            Task task = new Event(info, fromDateTime, toDateTime);
            tasks.add(task);
            ui.showTaskAdded(task, tasks.size());
        } catch (DateTimeParseException e) {
            ui.showError("Please enter event times as yyyy-MM-dd HHmm! :(");
        }
    }

    private static void listTasks(TaskList tasks, Ui ui) {
        ui.showTasks(tasks);
    }

    private static void completeTask(
            TaskList tasks, Ui ui, int taskNumber) {
        tasks.mark(taskNumber - 1);
        ui.showTaskCompleted(tasks.get(taskNumber - 1));
    }

    private static void uncompleteTask(
            TaskList tasks, Ui ui, int taskNumber) {
        tasks.unmark(taskNumber - 1);
        ui.showTaskUncompleted(tasks.get(taskNumber - 1));
    }

    private static void deleteTask(
            TaskList tasks, Ui ui, int taskNumber) {
        Task deletedTask = tasks.delete(taskNumber - 1);
        ui.showTaskDeleted(deletedTask, tasks.size());
    }

    private static void saveTasks(
            Storage storage, TaskList tasks) {
        try {
            storage.save(tasks.getTasks());
        } catch (IOException e) {
            System.out.println("I couldn't save your tasks! :(");
        }
    }

    private static void listTasksOnDate(
            TaskList tasks, Ui ui, String input) {
        input = input.trim();

        if (input.isEmpty()) {
            ui.showError("Please provide a date in yyyy-MM-dd format! :(");
            return;
        }

        try {
            LocalDate date = LocalDate.parse(input, INPUT_DATE_FORMAT);
            boolean found = false;

            ui.showMessage("Tasks occurring on "
                            + date.format(OUTPUT_DATE_FORMAT)
                            + ":");

            for (Task task : tasks) {
                if (task instanceof Deadline) {
                    Deadline deadline = (Deadline) task;

                    if (deadline.getDoBy().equals(date)) {
                        ui.showMessage(task.toString());
                        found = true;
                    }
                } else if (task instanceof Event) {
                    Event event = (Event) task;

                    LocalDate fromDate =
                            event.getFrom().toLocalDate();
                    LocalDate toDate =
                            event.getTo().toLocalDate();

                    if (!date.isBefore(fromDate) && !date.isAfter(toDate)) {
                        ui.showMessage(task.toString());
                        found = true;
                    }
                }
            }

            if (!found) {
                ui.showMessage("No deadlines or events found on that date.");
            }
        } catch (DateTimeParseException e) {
            ui.showError("Please enter the date as yyyy-MM-dd! :(");
        }
    }
}