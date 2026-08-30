package clover.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores Clover's tasks and provides list operations for commands.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied tasks.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to this list.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the specified zero-based index.
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the task at the specified zero-based index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the last task in this list.
     */
    public Task getLast() {
        return tasks.getLast();
    }

    /**
     * Returns the number of tasks in this list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the tasks in this list.
     */
    public List<Task> asList() {
        return tasks;
    }
}
