import java.time.LocalDate;

public class Task {
    private String description;
    private boolean completed;

    public Task(String description) {
        this.description = description;
        this.completed = false;
    }

    @Override
    public String toString() {
        String status = completed ? "[X] " : "[ ] ";

        return status + this.description;
    }

    public String getDescription() {
        return this.description;
    }

    public void markAsComplete() {
        this.completed = true;
    }

    public void markAsIncomplete() {
        this.completed = false;
    }

    public boolean isCompleted() {
        return this.completed;
    }
    
    public boolean occursOn(LocalDate date) {
        return false;
    }
}