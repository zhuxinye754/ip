package clover.command;

import clover.storage.Storage;
import clover.task.Task;
import clover.task.TaskList;
import clover.ui.Ui;

/**
 * Adds a task created from text that does not start with a known command.
 */
public class PlainTaskCommand extends Command {
    private final String description;

    /**
     * Creates a command for a plain task description.
     */
    public PlainTaskCommand(String description) {
        this.description = description;
    }

    /**
     * Adds and saves the plain task.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.add(new Task(description));
        saveTasks(tasks, ui, storage);
        ui.showPlainTask(tasks.getLast());
    }
}
