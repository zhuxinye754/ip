package clover.command;

import clover.exception.CloverException;
import clover.storage.Storage;
import clover.task.Task;
import clover.task.TaskList;
import clover.tutoree.TutoreeList;
import clover.ui.Ui;

/**
 * Marks an existing task as incomplete.
 */
public class UnmarkCommand extends Command {
    private final String taskNumber;

    /**
     * Creates a command for the supplied one-based task number.
     */
    public UnmarkCommand(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * Unmarks and saves the selected task.
     */
    @Override
    public void execute(TaskList tasks, TutoreeList tutorees, Ui ui, Storage storage) throws CloverException {
        int taskIndex = parseValidTaskIndex(taskNumber, tasks, "Please enter a valid task number to unmark.");
        Task task = tasks.get(taskIndex);
        task.markAsUndone();
        saveTasks(tasks, ui, storage);
        ui.showTaskUnmarked(task);
    }
}
