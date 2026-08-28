package gnaix;

import gnaix.task.Task;
import gnaix.task.TaskList;

import java.util.Scanner;

/**
 * Handles interactions with the user.
 */
public class Ui {
    private static final String SEPARATOR =
            "____________________________________________________________";

    private static final String BANNER =
            "  ____ _   _    _    _____  __\n"
                    + " / ___| \\ | |  / \\  |_ _\\ \\/ /\n"
                    + "| |  _|  \\| | / _ \\  | | \\  /\n"
                    + "| |_| | |\\  |/ ___ \\ | | /  \\\n"
                    + " \\____|_| \\_/_/   \\_\\___/_/\\_\\";

    private final Scanner scanner;

    public Ui() {
        scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        System.out.println(SEPARATOR);
        System.out.println(BANNER);
        System.out.println("Hello! I'm Gnaix");
        System.out.println("What can I do for you?");
    }

    public String readCommand() {
        return scanner.nextLine().trim();
    }

    public void showLine() {
        System.out.println(SEPARATOR);
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showError(String message) {
        System.out.println(message);
    }

    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    public void showTaskCompleted(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    public void showTaskUncompleted(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    public void showTasks(TaskList tasks) {
        System.out.println("Here are the tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    public void showGoodbye() {
        System.out.println(SEPARATOR);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(SEPARATOR);
    }
    
}