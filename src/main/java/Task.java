/**
 * Represents one task in the task list.
 */
public class Task {
    protected String description;
    protected TaskStatus status;

    public Task(String description) {
        this.description = description;
        this.status = TaskStatus.NOT_DONE;
    }

    private String getStatusIcon() {
        return ( status == TaskStatus.DONE ? "X" : " ");
    }

    public void markAsDone() {

        status = TaskStatus.DONE;
    }

    public void markAsUndone() {

        status = TaskStatus.NOT_DONE;
    }

    /** Returns this task's description for saving it to the data file. */
    public String getDescription() {
        return description;
    }

    /** Returns whether this task has been completed. */
    public boolean isDone() {
        return status == TaskStatus.DONE;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + this.description;
    }
}
