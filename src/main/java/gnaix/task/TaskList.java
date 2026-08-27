package gnaix.task;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents and manages the list of tasks.
 */
public class TaskList implements Iterable<Task> {
    private final ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public int size() {
        return tasks.size();
    }

    public Task delete(int index) {
        return tasks.remove(index);
    }

    public void mark(int index) {
        tasks.get(index).markAsComplete();
    }

    public void unmark(int index) {
        tasks.get(index).markAsIncomplete();
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public Task getLast() {
        return tasks.getLast();
    }

    @Override
    public Iterator<Task> iterator() {
        return tasks.iterator();
    }
}