package clover.command;

import clover.exception.CloverException;
import clover.storage.Storage;
import clover.task.Task;
import clover.task.TaskList;
import clover.tutoree.TutoreeList;
import clover.ui.Ui;

/**
 * Marks an existing task as complete.
 */
public class MarkCommand extends Command {
    private final String taskNumber;

    /**
     * Creates a command for the supplied one-based task number.
     */
    public MarkCommand(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * Marks and saves the selected task.
     */
    @Override
    public void execute(TaskList tasks, TutoreeList tutorees, Ui ui, Storage storage) throws CloverException {
        int taskIndex = parseValidTaskIndex(taskNumber, tasks, "Choose a valid quest number to complete.");
        Task task = tasks.get(taskIndex);
        task.markAsDone();
        saveTasks(tasks, ui, storage);
        ui.showTaskMarked(task);
    }

    @Override
    public CommandResponseStyle getResponseStyle() {
        return CommandResponseStyle.TASK_MARKED;
    }
}
