package clover.command;

import clover.exception.CloverException;
import clover.storage.Storage;
import clover.task.TaskList;
import clover.ui.Ui;

import java.io.IOException;

/** Represents one action that Clover can perform for the user. */
public abstract class Command {
    /** Performs this command using Clover's current collaborators. */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws CloverException;

    /** Returns whether this command ends the Clover application. */
    public boolean isExit() {
        return false;
    }

    /** Saves the task list and reports an error without stopping the command loop. */
    protected void saveTasks(TaskList tasks, Ui ui, Storage storage) {
        try {
            storage.save(tasks.asList());
        } catch (IOException | SecurityException e) {
            ui.showError("I could not save your tasks to the data file.");
        }
    }
}
