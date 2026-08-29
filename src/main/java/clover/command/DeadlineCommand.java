package clover.command;

import clover.exception.CloverException;
import clover.parser.Parser;
import clover.storage.Storage;
import clover.task.Deadline;
import clover.task.Event;
import clover.task.TaskList;
import clover.ui.Ui;

import java.time.LocalDate;

/** Adds a deadline task with a description and due date. */
public class DeadlineCommand extends Command {
    private static final String DEADLINE_MARKER = "/by";

    private final String arguments;

    /** Creates a command from the text after the {@code deadline} keyword. */
    public DeadlineCommand(String arguments) {
        this.arguments = arguments;
    }

    /** Validates, adds, and saves the deadline task. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws CloverException {
        int markerIndex = arguments.indexOf(DEADLINE_MARKER);
        if (markerIndex <= 0) {
            throw invalidFormat();
        }

        String description = arguments.substring(0, markerIndex).trim();
        String dueDate = arguments.substring(markerIndex + DEADLINE_MARKER.length()).trim();
        if (description.isEmpty() || dueDate.isEmpty()) {
            throw invalidFormat();
        }

        LocalDate date = Parser.parseDate(dueDate);
        tasks.add(new Deadline(description, date));
        saveTasks(tasks, ui, storage);
        ui.showTaskAdded(tasks.getLast(), tasks.size());
    }

    /** Creates the shared guidance message for malformed deadline commands. */
    private CloverException invalidFormat() {
        return new CloverException("Please use the format: deadline DESCRIPTION /by DUE DATE");
    }

}
