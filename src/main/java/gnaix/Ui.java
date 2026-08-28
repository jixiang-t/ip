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

    /**
     * Creates a user interface backed by standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Displays the application welcome message.
     */
    public void showWelcome() {
        System.out.println(SEPARATOR);
        System.out.println(BANNER);
        System.out.println("Hello! I'm Gnaix");
        System.out.println("What can I do for you?");
    }

    /**
     * Reads a command from standard input.
     *
     * @return User-entered command.
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Displays the standard separator line.
     */
    public void showLine() {
        System.out.println(SEPARATOR);
    }

    /**
     * Displays a message to the user.
     *
     * @param message Message to display.
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays an error message to the user.
     *
     * @param message Error message to display.
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Displays a message confirming that a task was added.
     *
     * @param task Task that was added.
     * @param taskCount Number of tasks after the addition.
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays a message confirming that a task was marked as complete.
     *
     * @param task Task that was marked as complete.
     */
    public void showTaskCompleted(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    /**
     * Displays a message confirming that a task was marked as incomplete.
     *
     * @param task Task that was marked as incomplete.
     */
    public void showTaskUncompleted(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    /**
     * Displays a message confirming that a task was deleted.
     *
     * @param task Task that was deleted.
     * @param taskCount Number of tasks remaining after the deletion.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays all tasks in the task list with their corresponding task numbers.
     *
     * @param tasks Task list to display.
     */
    public void showTasks(TaskList tasks) {
        System.out.println("Here are the tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    /**
     * Displays the application goodbye message.
     */
    public void showGoodbye() {
        System.out.println(SEPARATOR);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(SEPARATOR);
    }
    
}