package clover.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
/**
 * Represents one task that need to be done before a specific date/time
 * format for adding a new deadline tasks: deadline DESCRIPTION /by DEADLINE
 */
public class Deadline extends Task{
    private final LocalDate endBy;
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy");

    public Deadline(String description, LocalDate endBy) {
        super(description);
        this.endBy = endBy;
    }

    /** Returns the deadline text for saving it to the data file. */
    public LocalDate getEndBy() {
        return endBy;
    }

    @Override
    public String toString() {
        return "[D] " + super.toString()
                + " (by: " + endBy.format(DISPLAY_FORMAT) + ")";
    }
}
