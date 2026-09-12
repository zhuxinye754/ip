package clover.command;

import java.io.IOException;

import clover.exception.CloverException;
import clover.parser.Parser;
import clover.storage.Storage;
import clover.task.TaskList;
import clover.ui.Ui;

/**
 * Represents one action that Clover can perform for the user.
 */
public abstract class Command {
    /**
     * Performs this command using Clover's current collaborators.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws CloverException;

    /**
     * Returns whether this command ends the Clover application.
     */
    public boolean isExit() {
        return false;
    }

    /**
     * Returns the visual style to apply to this command's response.
     */
    public CommandResponseStyle getResponseStyle() {
        return CommandResponseStyle.STANDARD;
    }

    /**
     * Validates a user-entered task number and returns its zero-based task-list index.
     *
     * @param taskNumber the one-based task number entered by the user
     * @param tasks the current task list
     * @param invalidTaskNumberMessage the message to display for an invalid task number
     * @return the validated zero-based task-list index
     * @throws CloverException if the task number does not identify a task in the list
     */
    protected int parseValidTaskIndex(String taskNumber, TaskList tasks, String invalidTaskNumberMessage)
            throws CloverException {
        if (!Parser.isValidTaskNumber(taskNumber, tasks.size())) {
            throw new CloverException(invalidTaskNumberMessage);
        }

        int taskIndex = Parser.parseTaskIndex(taskNumber);
        assert taskIndex >= 0 && taskIndex < tasks.size()
                : "A validated one-based task number must map to an existing list index.";
        return taskIndex;
    }

    /** Saves the task list and reports an error without stopping the command loop. */
    protected void saveTasks(TaskList tasks, Ui ui, Storage storage) {
        try {
            storage.save(tasks.asList());
        } catch (IOException | SecurityException exception) {
            ui.showError("I could not save your tasks to the data file.");
        }
    }
}
