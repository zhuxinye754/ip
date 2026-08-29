package clover.task;

/**
 * Represents one task  without any date/time attached to them
 * format for adding a new todo tasks: todo DESCRIPTION
 */
public class ToDo extends Task{
    public ToDo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T] " + super.toString();
    }
}
