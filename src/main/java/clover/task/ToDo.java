package clover.task;

/**
 * Represents one task  without any date/time attached to them
 * format for adding a new todo tasks: todo DESCRIPTION
 */
public class ToDo extends Task {
    /** Creates a todo task with the given description. */
    public ToDo(String description) {
        super(description);
    }

    /** Returns this todo in Clover's display format. */
    @Override
    public String toString() {
        return "[T] " + super.toString();
    }
}
