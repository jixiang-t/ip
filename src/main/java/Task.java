public class Task {
    private String description;
    private boolean completed;
    private String type;
    private String from;
    private String to;

    public Task(String description) {
        this.description = description;
        this.completed = false;
        this.type = "T";
    }

    public Task(String description, String type) {
        this.description = description;
        this.completed = false;
        this.type = type;
    }

    public Task(String description, String type, String from, String to) {
        this.description = description;
        this.completed = false;
        this.type = type;
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        String status = completed ? "[X]" : "[ ]";

        if (type.equals("D")) {
            return "[D]" + status + " " + description + " (by: " + from + ")";
        }

        if (type.equals("E")) {
            return "[E]" + status + " " + description
                    + " (from: " + from + " to: " + to + ")";
        }

        return "[T]" + status + " " + description;
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

}
