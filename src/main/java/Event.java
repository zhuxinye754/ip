/**
 * Represents one task  with a specific start and end date/time
 * format for adding a new event tasks: event DESCRIPTION /from START /to END
 */
public class Event extends Task{
    private final String start;
    private final String end;
    public Event(String description, String start, String end) {
        super(description);
        this.start = start;
        this.end = end
        ;
    }

    /** Returns the event start text for saving it to the data file. */
    public String getStart() {
        return start;
    }

    /** Returns the event end text for saving it to the data file. */
    public String getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "[E] " + super.toString() + " (from: " + this.start + " to: " + this.end + ")";
    }
}
