import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Event extends Task {
    private LocalDateTime from;
    private LocalDateTime to;
    private static final DateTimeFormatter OUTPUT_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return this.from;
    }

    public LocalDateTime getTo() {
        return this.to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + this.from.format(OUTPUT_DATE_TIME_FORMAT)
                + " to: " + this.to.format(OUTPUT_DATE_TIME_FORMAT) + ")";
    }
}
