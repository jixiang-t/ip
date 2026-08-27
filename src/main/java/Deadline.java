public class Deadline extends Task {
    private String doBy;

    public Deadline(String description, String doBy) {
        super(description);
        this.doBy = doBy;
    }

    public String getDoBy() {
        return this.doBy;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " +  this.doBy + ")";
    }
}
