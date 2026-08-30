package clover.command;

import java.util.ArrayList;

import clover.exception.CloverException;
import clover.parser.Parser;
import clover.storage.Storage;
import clover.task.Task;
import clover.task.TaskList;
import clover.ui.Ui;

/**
 * Finds a task by searching for a keyword in the task description from Clover's task list.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a FindCommand for the supplied search keyword.
     */
    public FindCommand(String argument) throws CloverException{
        String trimmed = argument.trim();
        if (trimmed.isEmpty()) {
            throw new CloverException("Please enter a keyword to search for.");
        }
        this.keyword = trimmed;
    }

    /**
     * Adds tasks whose description contains the keyword and displays them via the UI.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws CloverException {
        ArrayList<Task> result = new ArrayList<>();
        for (Task t : tasks.asList()) {
            if (t.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(t);
            }
        }
        ui.showFindResults(result);
    }
}