package clover.command;

import clover.exception.CloverException;
import clover.parser.Parser;
import clover.storage.Storage;
import clover.task.Task;
import clover.task.TaskList;
import clover.ui.Ui;

/** Removes an existing task from Clover's task list. */
public class DeleteCommand extends Command {
    private final String taskNumber;

    /** Creates a command for the supplied one-based task number. */
    public DeleteCommand(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    /** Removes and saves the selected task. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws CloverException {
        if (!Parser.isValidTaskNumber(taskNumber, tasks.size())) {
            throw new CloverException("Please enter a valid task number to delete.");
        }
        Task deletedTask = tasks.remove(Parser.parseTaskIndex(taskNumber));
        saveTasks(tasks, ui, storage);
        ui.showTaskDeleted(deletedTask, tasks.size());
    }
}
