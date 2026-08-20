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
