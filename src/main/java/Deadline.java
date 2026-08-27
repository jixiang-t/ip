import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class Deadline extends Task {
    private LocalDate doBy;
    private static final DateTimeFormatter OUTPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy");

    public Deadline(String description, LocalDate doBy) {
        super(description);
        this.doBy = doBy;
    }

    public LocalDate getDoBy() {
        return this.doBy;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString()
                + " (by: " + this.doBy.format(OUTPUT_DATE_FORMAT) + ")";
    }
}
