import java.util.Scanner;

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

        Task[] tasks = new Task[100];
        int taskCounter = 0;

        while (true) {
            System.out.println(separator);
            String cmd = scanner.nextLine();

            if (cmd.equals("bye")) {
                break;
            } else if (cmd.equals("list")) {
                listTasks(tasks, taskCounter);
            } else if (cmd.startsWith("mark ")) {
                String[] taskData = cmd.split(" ");
                int taskNumber = Integer.parseInt(taskData[1]);
                completeTask(tasks,  taskNumber);
            } else if (cmd.startsWith("unmark ")) {
                String[] taskData = cmd.split(" ");
                int taskNumber = Integer.parseInt(taskData[1]);
                uncompleteTask(tasks,  taskNumber);
            } else if (cmd.startsWith("todo ")) {
                String input = cmd.substring(5);
                addToDo(tasks, taskCounter, input);
                taskCounter++;
            } else if (cmd.startsWith("deadline ")) {
                String input = cmd.substring(9);
                addDeadline(tasks, taskCounter, input);
                taskCounter++;
            } else if (cmd.startsWith("event ")) {
                String input = cmd.substring(6);
                addEvent(tasks, taskCounter, input);
                taskCounter++;
            } else {
                tasks[taskCounter] = new Task(cmd);
                taskCounter++;
                System.out.println("added: " + cmd);
            }

        }

        System.out.println(separator);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(separator);
    }

    private static void addToDo(Task[] tasks, int taskNumber, String description) {
        tasks[taskNumber] = new Todo(description);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + tasks[taskNumber]);
        System.out.println("Now you have " + (taskNumber + 1) + " tasks in the list.");
    }

    private static void addDeadline(Task[] tasks, int taskNumber, String description) {
        String[] parts = description.split(" /by ", 2);
        String info = parts[0];
        String by = parts[1];
        tasks[taskNumber] = new Deadline(info, by);

        System.out.println("Got it. I've added this task:");
        System.out.println("  " + tasks[taskNumber]);
        System.out.println("Now you have " + (taskNumber + 1) + " tasks in the list.");
    }

    private static void addEvent(Task[] tasks, int taskNumber, String description) {
        String[] parts = description.split(" /from ", 2);
        String info = parts[0];

        String[] times = parts[1].split(" /to ", 2);
        String from = times[0];
        String to = times[1];
        tasks[taskNumber] = new Event(info, from, to);

        System.out.println("Got it. I've added this task:");
        System.out.println("  " + tasks[taskNumber]);
        System.out.println("Now you have " + (taskNumber + 1) + " tasks in the list.");
    }

    private static void listTasks(Task[] tasks, int taskCount) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + ". " + tasks[i]);
        }
    }

    private static void completeTask(Task[] tasks, int taskNumber) {
        tasks[taskNumber - 1].markAsComplete();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + tasks[taskNumber - 1]);
    }

    private static void uncompleteTask(Task[] tasks, int taskNumber) {
        tasks[taskNumber - 1].markAsIncomplete();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + tasks[taskNumber - 1]);
    }
}
