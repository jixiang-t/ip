import java.time.LocalDate;

public class Deadline extends Task {
    private LocalDate doBy;

    public Deadline(String description, LocalDate doBy) {
        super(description);
        this.doBy = doBy;
    }

    public LocalDate getDoBy() {
        return this.doBy;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.doBy + ")";
    }
}
