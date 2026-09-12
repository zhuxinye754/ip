package clover.command;

import clover.exception.CloverException;
import clover.parser.Parser;
import clover.parser.TaskArguments;
import clover.storage.Storage;
import clover.task.TaskList;
import clover.task.ToDo;
import clover.tutoree.TutoreeList;
import clover.ui.Ui;

/**
 * Adds a todo task.
 */
public class ToDoCommand extends Command {
    private final String description;

    /**
     * Creates a command with the todo description entered by the user.
     */
    public ToDoCommand(String description) {
        this.description = description;
    }

    /**
     * Validates, adds, and saves the todo task.
     */
    @Override
    public void execute(TaskList tasks, TutoreeList tutorees, Ui ui, Storage storage) throws CloverException {
        TaskArguments parsedArguments = Parser.parseTaskArguments(description);
        String taskDescription = parsedArguments.getTaskArguments();
        if (taskDescription.isEmpty()) {
            throw new CloverException("The description of a todo cannot be empty.");
        }
        String tutoreeName = validateTutoreeName(parsedArguments.getTutoreeName(), tutorees);
        tasks.add(new ToDo(taskDescription, tutoreeName));
        saveTasks(tasks, ui, storage);
        ui.showTaskAdded(tasks.getLast(), tasks.size());
    }

    @Override
    public CommandResponseStyle getResponseStyle() {
        return CommandResponseStyle.TASK_ADDED;
    }
}
