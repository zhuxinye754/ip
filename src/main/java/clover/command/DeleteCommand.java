package clover.command;

import clover.exception.CloverException;
import clover.storage.Storage;
import clover.task.Task;
import clover.task.TaskList;
import clover.tutoree.TutoreeList;
import clover.ui.Ui;

/**
 * Removes an existing task from Clover's task list.
 */
public class DeleteCommand extends Command {
    private final String taskNumber;

    /**
     * Creates a command for the supplied one-based task number.
     */
    public DeleteCommand(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * Removes and saves the selected task.
     */
    @Override
    public void execute(TaskList tasks, TutoreeList tutorees, Ui ui, Storage storage) throws CloverException {
        int taskIndex = parseValidTaskIndex(taskNumber, tasks, "Please enter a valid task number to delete.");
        Task deletedTask = tasks.remove(taskIndex);
        saveTasks(tasks, ui, storage);
        ui.showTaskDeleted(deletedTask, tasks.size());
    }

    @Override
    public CommandResponseStyle getResponseStyle() {
        return CommandResponseStyle.TASK_DELETED;
    }
}
