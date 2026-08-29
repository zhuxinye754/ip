package clover.command;

import clover.exception.CloverException;
import clover.parser.Parser;
import clover.storage.Storage;
import clover.task.Event;
import clover.task.TaskList;
import clover.ui.Ui;

import java.time.LocalDate;

/** Adds an event task with a description, start date, and end date. */
public class EventCommand extends Command {
    private static final String FROM_MARKER = "/from";
    private static final String TO_MARKER = "/to";

    private final String arguments;

    /** Creates a command from the text after the {@code event} keyword. */
    public EventCommand(String arguments) {
        this.arguments = arguments;
    }

    /** Validates, adds, and saves the event task. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws CloverException {
        int fromIndex = arguments.indexOf(FROM_MARKER);
        int toIndex = arguments.indexOf(TO_MARKER);
        if (fromIndex <= 0 || toIndex <= fromIndex + FROM_MARKER.length()) {
            throw invalidFormat();
        }

        String description = arguments.substring(0, fromIndex).trim();
        String startDate = arguments.substring(fromIndex + FROM_MARKER.length(), toIndex).trim();
        String endDate = arguments.substring(toIndex + TO_MARKER.length()).trim();
        if (description.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            throw invalidFormat();
        }

        LocalDate start = Parser.parseDate(startDate);
        LocalDate end = Parser.parseDate(endDate);
        tasks.add(new Event(description, start, end));
        saveTasks(tasks, ui, storage);
        ui.showTaskAdded(tasks.getLast(), tasks.size());
    }

    /** Creates the shared guidance message for malformed event commands. */
    private CloverException invalidFormat() {
        return new CloverException("Please use the format: event DESCRIPTION /from START /to END");
    }
}