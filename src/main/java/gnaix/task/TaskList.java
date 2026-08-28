package gnaix.task;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents and manages the list of tasks.
 */
public class TaskList implements Iterable<Task> {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied tasks.
     *
     * @param tasks Tasks to store in the list.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at the specified zero-based index.
     *
     * @param index Zero-based index of the task.
     * @return Task at the specified index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return Number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Removes and returns the task at the specified index.
     *
     * @param index Zero-based index of the task to remove.
     * @return Removed task.
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Marks the task at the specified index as complete.
     *
     * @param index Zero-based index of the task.
     */
    public void mark(int index) {
        tasks.get(index).markAsComplete();
    }

    /**
     * Marks the task at the specified index as incomplete.
     *
     * @param index Zero-based index of the task.
     */
    public void unmark(int index) {
        tasks.get(index).markAsIncomplete();
    }

    /**
     * Returns the underlying list of tasks.
     *
     * @return List containing the tasks.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Returns the last task in the list.
     *
     * @return Last task in the list.
     */
    public Task getLast() {
        return tasks.getLast();
    }

    /**
     * Returns an iterator over the tasks.
     *
     * @return Iterator over the task list.
     */
    @Override
    public Iterator<Task> iterator() {
        return tasks.iterator();
    }
}