/**
 * Represents one task that need to be done before a specific date/time
 * format for adding a new deadline tasks: deadline DESCRIPTION /by DEADLINE
 */
public class Deadline extends Task{
    private String endBy;
    public Deadline(String description, String endBy) {
        super(description);
        this.endBy = endBy;
    }

    /** Returns the deadline text for saving it to the data file. */
    public String getEndBy() {
        return endBy;
    }

    @Override
    public String toString() {
        return "[D] " + super.toString() + " (by: " + this.endBy + ")";
    }
}
