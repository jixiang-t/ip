import java.io.IOException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Gnaix {
    private static final DateTimeFormatter INPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter INPUT_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    private static final DateTimeFormatter OUTPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy");

    private static final DateTimeFormatter OUTPUT_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String separator = "____________________________________________________________";
        String banner = "  ____ _   _    _    _____  __\n"
                + " / ___| \\ | |  / \\  |_ _\\ \\/ /\n"
                + "| |  _|  \\| | / _ \\  | | \\  /\n"
                + "| |_| | |\\  |/ ___ \\ | | /  \\\n"
                + " \\____|_| \\_/_/   \\_\\___/_/\\_\\";

        System.out.println(separator);
        System.out.println(banner);
        System.out.println("Hello! I'm Gnaix");
        System.out.println("What can I do for you?");

        Storage storage = new Storage(Path.of("data", "gnaix.txt"));
        TaskList tasks;
        try {
            tasks = new TaskList(storage.load());
        } catch (IOException e) {
            System.out.println("I couldn't load your tasks. Starting with an empty list! :(");
            tasks = new TaskList();
        }
        boolean isRunning = true;

        while (isRunning) {
            System.out.println(separator);
            String cmd = scanner.nextLine().trim();

            if (cmd.isEmpty()) {
                System.out.println("Please enter a command! :(");
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
                    listTasks(tasks);
                    break;

                case MARK:
                    try {
                        int taskNumber = Integer.parseInt(arguments);
                        if (taskNumber < 1 || taskNumber > tasks.size()) {
                            System.out.println("That task number does not exist! :(");
                        } else {
                            completeTask(tasks, taskNumber);
                            saveTasks(storage, tasks);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("That task number is not a number! :(");
                    }
                    break;

                case UNMARK:
                    try {
                        int taskNumber = Integer.parseInt(arguments);
                        if (taskNumber < 1 || taskNumber > tasks.size()) {
                            System.out.println("That task number does not exist! :(");
                        } else {
                            uncompleteTask(tasks, taskNumber);
                            saveTasks(storage, tasks);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("That task number is not a number! :(");
                    }
                    break;

                case DELETE:
                    try {
                        int taskNumber = Integer.parseInt(arguments);

                        if (taskNumber < 1 || taskNumber > tasks.size()) {
                            System.out.println("That task number does not exist! :(");
                        } else {
                            deleteTask(tasks, taskNumber);
                            saveTasks(storage, tasks);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("That task number is not a number! :(");
                    }
                    break;

                case TODO:
                    addToDo(tasks, arguments);
                    saveTasks(storage, tasks);
                    break;

                case DEADLINE:
                    addDeadline(tasks, arguments);
                    saveTasks(storage, tasks);
                    break;

                case EVENT:
                    addEvent(tasks, arguments);
                    saveTasks(storage, tasks);
                    break;

                case DATE:
                    listTasksOnDate(tasks, arguments);
                    break;

                case UNKNOWN:
                    System.out.println("That's not a valid command! :(");
                    break;
            }

        }

        System.out.println(separator);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(separator);
    }

    private static void addToDo(TaskList tasks, String description) {
        description = description.trim();
        if (description.isEmpty()) {
            System.out.println("NO DESCRIPTION GIVEN! :(");
            return;
        }

        tasks.add(new Todo(description));
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + tasks.getLast());
        System.out.println("Now you have " + (tasks.size()) + " tasks in the list.");
    }

    private static void addDeadline(TaskList tasks, String description) {
        String[] parts = description.split(" /by ", 2);

        if (parts.length < 2) {
            System.out.println("A deadline needs a description and a /by date! :(");
            return;
        }

        String info = parts[0].trim();
        String by = parts[1].trim();

        if (info.isEmpty() || by.isEmpty()) {
            System.out.println("A deadline needs a description and a /by date! :(");
            return;
        }

        try {
            LocalDate deadlineDate = LocalDate.parse(by, INPUT_DATE_FORMAT);
            tasks.add(new Deadline(info, deadlineDate));

            System.out.println("Got it. I've added this task:");
            System.out.println("  " + tasks.getLast());
            System.out.println("Now you have " + tasks.size() + " tasks in the list.");

        } catch (DateTimeParseException e) {
            System.out.println("Please enter the deadline as yyyy-MM-dd! :(");
        }
    }

    private static void addEvent(TaskList tasks, String description) {
        String[] parts = description.split(" /from ", 2);

        if (parts.length < 2) {
            System.out.println("Not enough info given! :(");
            return;
        }

        String info = parts[0].trim();
        String[] times = parts[1].split(" /to ", 2);

        if (info.isEmpty() || times.length < 2) {
            System.out.println("An event needs a description and timings! :(");
            return;
        }

        String from = times[0].trim();
        String to = times[1].trim();

        if (from.isEmpty() || to.isEmpty()) {
            System.out.println("An event needs a /from time, and /to time! :(");
            return;
        }

        try {
            LocalDateTime fromDateTime = LocalDateTime.parse(from, INPUT_DATE_TIME_FORMAT);

            LocalDateTime toDateTime = LocalDateTime.parse(to, INPUT_DATE_TIME_FORMAT);

            tasks.add(new Event(info, fromDateTime, toDateTime));

            System.out.println("Got it. I've added this task:");
            System.out.println("  " + tasks.getLast());
            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        } catch (DateTimeParseException e) {
            System.out.println("Please enter event times as yyyy-MM-dd HHmm! :(");
        }
    }

    private static void listTasks(TaskList tasks) {
        System.out.println("Here are the tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    private static void completeTask(TaskList tasks, int taskNumber) {
        tasks.mark(taskNumber - 1);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + tasks.get(taskNumber - 1));
    }

    private static void uncompleteTask(TaskList tasks, int taskNumber) {
        tasks.unmark(taskNumber - 1);
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + tasks.get(taskNumber - 1));
    }

    private static void deleteTask(TaskList tasks, int taskNumber) {
        Task deletedTask = tasks.delete(taskNumber - 1);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + deletedTask);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }

    private static void saveTasks(Storage storage, TaskList tasks) {
        try {
            storage.save(tasks.getTasks());
        } catch (IOException e) {
            System.out.println("I couldn't save your tasks! :(");
        }
    }

    private static void listTasksOnDate(TaskList tasks, String input) {
        input = input.trim();
        if (input.isEmpty()) {
            System.out.println("Please provide a date in yyyy-MM-dd format! :(");
            return;
        }

        try {
            LocalDate date = LocalDate.parse(input, INPUT_DATE_FORMAT);
            boolean found = false;

            System.out.println("Tasks occurring on " + date.format(OUTPUT_DATE_FORMAT) + ":");
            for (Task task : tasks) {
                if (task instanceof Deadline) {
                    Deadline deadline = (Deadline) task;
                    if (deadline.getDoBy().equals(date)) {
                        System.out.println(task);
                        found = true;
                    }
                } else if (task instanceof Event) {
                    Event event = (Event) task;

                    LocalDate fromDate = event.getFrom().toLocalDate();
                    LocalDate toDate = event.getTo().toLocalDate();

                    if (!date.isBefore(fromDate) && !date.isAfter(toDate)) {
                        System.out.println(task);
                        found = true;
                    }
                }
            }

            if (!found) {
                System.out.println("No deadlines or events found on that date.");
            }
        } catch (DateTimeParseException e) {
            System.out.println("Please enter the date as yyyy-MM-dd! :(");
        }
    }
}
