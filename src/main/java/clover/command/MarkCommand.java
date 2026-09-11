package clover.command;

import clover.exception.CloverException;
import clover.parser.Parser;
import clover.storage.Storage;
import clover.task.Task;
import clover.task.TaskList;
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
    public void execute(TaskList tasks, Ui ui, Storage storage) throws CloverException {
        if (!Parser.isValidTaskNumber(taskNumber, tasks.size())) {
            throw new CloverException("Please enter a valid task number to mark.");
        }
        int taskIndex = Parser.parseTaskIndex(taskNumber);
        assert taskIndex >= 0 && taskIndex < tasks.size()
                : "A validated one-based task number must map to an existing list index.";
        Task task = tasks.get(taskIndex);
        task.markAsDone();
        saveTasks(tasks, ui, storage);
        ui.showTaskMarked(task);
    }
}
