import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Path;

public class Gnaix {
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
        ArrayList<Task> tasks;
        try {
            tasks = storage.load();
        } catch (IOException e) {
            System.out.println("I couldn't load your tasks. Starting with an empty list! :(");
            tasks = new ArrayList<>();
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

                case UNKNOWN:
                    System.out.println("That's not a valid command! :(");
                    break;
            }

        }

        System.out.println(separator);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(separator);
    }

    private static void addToDo(ArrayList<Task> tasks, String description) {
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

    private static void addDeadline(ArrayList<Task> tasks, String description) {
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

        tasks.add(new Deadline(info, by));

        System.out.println("Got it. I've added this task:");
        System.out.println("  " + tasks.getLast());
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }

    private static void addEvent(ArrayList<Task> tasks, String description) {
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

        tasks.add(new Event(info, from, to));

        System.out.println("Got it. I've added this task:");
        System.out.println("  " + tasks.getLast());
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }

    private static void listTasks(ArrayList<Task> tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    private static void completeTask(ArrayList<Task> tasks, int taskNumber) {
        tasks.get(taskNumber - 1).markAsComplete();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + tasks.get(taskNumber - 1));
    }

    private static void uncompleteTask(ArrayList<Task> tasks, int taskNumber) {
        tasks.get(taskNumber - 1).markAsIncomplete();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + tasks.get(taskNumber - 1));
    }

    private static void deleteTask(ArrayList<Task> tasks, int taskNumber) {
        Task deletedTask = tasks.remove(taskNumber - 1);

        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + deletedTask);
        System.out.println("Now you have " + (tasks.size()) + " tasks in the list.");
    }

    private static void saveTasks(Storage storage, ArrayList<Task> tasks) {
        try {
            storage.save(tasks);
        } catch (IOException e) {
            System.out.println("I couldn't save your tasks! :(");
        }
    }
}
