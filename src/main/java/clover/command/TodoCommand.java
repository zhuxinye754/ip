package clover.command;

import clover.exception.CloverException;
import clover.storage.Storage;
import clover.task.TaskList;
import clover.task.ToDo;
import clover.ui.Ui;

/** Adds a todo task. */
public class TodoCommand extends Command {
    private final String description;

    /** Creates a command with the todo description entered by the user. */
    public TodoCommand(String description) {
        this.description = description;
    }

    /** Validates, adds, and saves the todo task. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws CloverException {
        if (description.isEmpty()) {
            throw new CloverException("The description of a todo cannot be empty.");
        }
        tasks.add(new ToDo(description));
        saveTasks(tasks, ui, storage);
        ui.showTaskAdded(tasks.getLast(), tasks.size());
    }
}
