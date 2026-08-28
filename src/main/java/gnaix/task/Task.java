package gnaix.task;

import java.time.LocalDate;

/**
 * Represents a task with a description and completion status.
 */
public class Task {
    private String description;
    private boolean completed;

    /**
     * Creates a task with the given description.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.completed = false;
    }

    @Override
    public String toString() {
        String status = completed ? "[X] " : "[ ] ";

        return status + this.description;
    }

    /**
     * Returns the task description.
     *
     * @return Description of the task.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Marks the task as complete.
     */
    public void markAsComplete() {
        this.completed = true;
    }

    /**
     * Marks the task as incomplete.
     */
    public void markAsIncomplete() {
        this.completed = false;
    }

    /**
     * Returns whether the task is complete.
     *
     * @return True if the task is complete.
     */
    public boolean isCompleted() {
        return this.completed;
    }

    /**
     * Returns whether the task occurs on the specified date.
     *
     * @param date Date to check.
     * @return False because a generic task has no associated date.
     */
    public boolean occursOn(LocalDate date) {
        return false;
    }
}