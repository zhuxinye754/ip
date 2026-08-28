import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents one task  with a specific start and end date/time
 * format for adding a new event tasks: event DESCRIPTION /from START /to END
 */
public class Event extends Task{
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy");

    private final LocalDate start;
    private final LocalDate end;
    public Event(String description, LocalDate start, LocalDate end) {
        super(description);
        this.start = start;
        this.end = end
        ;
    }

    /** Returns the event start text for saving it to the data file. */
    public LocalDate getStart() {
        return start;
    }

    /** Returns the event end text for saving it to the data file. */
    public LocalDate getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "[E] " + super.toString()
                + " (from: " + start.format(DISPLAY_FORMAT)
                + " to: " + end.format(DISPLAY_FORMAT) + ")";
    }
}
