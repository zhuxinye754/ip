package clover.task;

import java.util.ArrayList;
import java.util.List;

/** Manages Clover's ordered collection of tasks. */
public class TaskList {
    private ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /** Creates a task list containing the supplied saved tasks. */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /** Adds a task to the end of the list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /** Removes and returns the task at the supplied zero-based index. */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /** Returns the task at the supplied zero-based index. */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** Returns the most recently added task. */
    public Task getLast() {
        return tasks.getLast();
    }

    /** Returns the number of tasks in this list. */
    public int size() {
        return tasks.size();
    }

    /** Returns the tasks in their current display order. */
    public List<Task> asList() {
        return tasks;
    }
}
