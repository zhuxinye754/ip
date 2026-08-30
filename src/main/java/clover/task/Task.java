package clover.task;

/**
 * Represents one task in the task list.
 */
public class Task {
    protected String description;
    protected TaskStatus status;

    /**
     * Creates an incomplete task with the supplied description.
     */
    public Task(String description) {
        this.description = description;
        this.status = TaskStatus.NOT_DONE;
    }

    private String getStatusIcon() {
        return status == TaskStatus.DONE ? "X" : " ";
    }

    /**
     * Marks this task as complete.
     */
    public void markAsDone() {
        status = TaskStatus.DONE;
    }

    /**
     * Marks this task as incomplete.
     */
    public void markAsUndone() {
        status = TaskStatus.NOT_DONE;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Returns whether this task has been completed.
     */
    public boolean isDone() {
        return status == TaskStatus.DONE;
    }

    /**
     * Returns this task in Clover's display format.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
