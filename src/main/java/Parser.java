import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/** Converts raw user input into command and value information for Clover. */
public class Parser {
    /** Commands that Clover accepts from the user. */
    public enum Command {
        LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, BYE
    }

    /** Identifies the command at the start of an input line, or returns null for a plain task. */
    public static Command parseCommand(String input) {
        String firstWord = input.trim().split("\\s+", 2)[0].toUpperCase();
        try {
            return Command.valueOf(firstWord);
        } catch (IllegalArgumentException e) {
            return null;
        }
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
