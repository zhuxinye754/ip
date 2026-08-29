package clover.parser;


import clover.command.Command;
import clover.command.ListCommand;
import clover.exception.CloverException;
import clover.command.EventCommand;
import clover.command.MarkCommand;
import clover.command.TodoCommand;
import clover.command.UnmarkCommand;
import clover.command.DeadlineCommand;
import clover.command.DeleteCommand;
import clover.command.ExitCommand;
import clover.command.PlainTaskCommand;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/** Converts raw user input into executable Clover commands. */
public class Parser {
    /** Creates the command represented by one complete line of user input. */
    public static Command parse(String input) throws CloverException {
        if (input.isBlank()) {
            throw new CloverException("Please enter a command or task description.");
        }

        String[] parts = input.trim().split("\\s+", 2);
        String commandWord = parts[0].toLowerCase();
        String arguments = parts.length == 2 ? parts[1] : "";

        return switch (commandWord) {
        case "list" -> new ListCommand();
        case "mark" -> new MarkCommand(arguments);
        case "unmark" -> new UnmarkCommand(arguments);
        case "todo" -> new TodoCommand(arguments);
        case "deadline" -> new DeadlineCommand(arguments);
        case "event" -> new EventCommand(arguments);
        case "delete" -> new DeleteCommand(arguments);
        case "bye" -> new ExitCommand();
        default -> new PlainTaskCommand(input);
        };
    }

    /** Returns whether the input is a valid one-based task number in the current list. */
    public static boolean isValidTaskNumber(String input, int taskCount) {
        try {
            int taskNumber = Integer.parseInt(input.trim());
            return taskNumber >= 1 && taskNumber <= taskCount;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** Converts a valid one-based task number to its zero-based list index. */
    public static int parseTaskIndex(String input) {
        return Integer.parseInt(input.trim()) - 1;
    }

    /** Parses a date entered by the user in ISO yyyy-MM-dd format. */
    public static LocalDate parseDate(String text) throws CloverException {
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException e) {
            throw new CloverException("Please enter dates in the format yyyy-MM-dd.");
        }
    }
}
