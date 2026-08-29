package clover.command;

import clover.exception.CloverException;
import clover.parser.Parser;
import clover.storage.Storage;
import clover.task.Task;
import clover.task.TaskList;
import clover.ui.Ui;

/** Marks an existing task as incomplete. */
public class UnmarkCommand extends Command {
    private final String taskNumber;

    /** Creates a command for the supplied one-based task number. */
    public UnmarkCommand(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    /** Unmarks and saves the selected task. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws CloverException {
        if (!Parser.isValidTaskNumber(taskNumber, tasks.size())) {
            throw new CloverException("Please enter a valid task number to unmark.");
        }
        Task task = tasks.get(Parser.parseTaskIndex(taskNumber));
        task.markAsUndone();
        saveTasks(tasks, ui, storage);
        ui.showTaskUnmarked(task);
    }
}
