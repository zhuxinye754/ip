package clover.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import clover.command.DeadlineCommand;
import clover.command.DeleteCommand;
import clover.command.EventCommand;
import clover.command.ExitCommand;
import clover.command.FindCommand;
import clover.command.ListCommand;
import clover.command.MarkCommand;
import clover.command.PlainTaskCommand;
import clover.command.ToDoCommand;
import clover.command.UnmarkCommand;
import clover.exception.CloverException;

/** Tests parsing of user input into Clover commands and values. */
class ParserTest {

    @Test
    void parse_knownCommandWords_correctCommandTypesReturned() throws CloverException {
        assertInstanceOf(ListCommand.class, Parser.parse("list"));
        assertInstanceOf(MarkCommand.class, Parser.parse("mark 1"));
        assertInstanceOf(UnmarkCommand.class, Parser.parse("unmark 1"));
        assertInstanceOf(ToDoCommand.class, Parser.parse("todo read book"));
        assertInstanceOf(DeadlineCommand.class, Parser.parse("deadline submit /by 2026-09-01"));
        assertInstanceOf(EventCommand.class, Parser.parse("event meeting /from 2026-09-01 /to 2026-09-02"));
        assertInstanceOf(DeleteCommand.class, Parser.parse("delete 1"));
        assertInstanceOf(FindCommand.class, Parser.parse("find book"));
        assertInstanceOf(ExitCommand.class, Parser.parse("bye"));
    }

    @Test
    void parse_commandWordWithDifferentCapitalisation_correctCommandTypeReturned() throws CloverException {
        assertInstanceOf(ToDoCommand.class, Parser.parse("TODO read book"));
    }

    @Test
    void parse_unknownCommandWord_plainTaskCommandReturned() throws CloverException {
        assertInstanceOf(PlainTaskCommand.class, Parser.parse("read book"));
    }

    @Test
    void parse_blankInput_exceptionThrown() {
        CloverException exception = assertThrows(CloverException.class, () -> Parser.parse("   "));

        assertEquals("Please enter a command or task description.", exception.getMessage());
    }

    @Test
    void isValidTaskNumber_firstAndLastTaskNumber_trueReturned() {
        assertTrue(Parser.isValidTaskNumber("1", 3));
        assertTrue(Parser.isValidTaskNumber("3", 3));
    }

    @Test
    void isValidTaskNumber_numberWithSurroundingWhitespace_trueReturned() {
        assertTrue(Parser.isValidTaskNumber(" 2 ", 3));
    }

    @Test
    void isValidTaskNumber_zeroOrNegativeNumber_falseReturned() {
        assertFalse(Parser.isValidTaskNumber("0", 3));
        assertFalse(Parser.isValidTaskNumber("-1", 3));
    }

    @Test
    void isValidTaskNumber_numberGreaterThanTaskCount_falseReturned() {
        assertFalse(Parser.isValidTaskNumber("4", 3));
        assertFalse(Parser.isValidTaskNumber("1", 0));
    }

    @Test
    void isValidTaskNumber_nonIntegerInput_falseReturned() {
        assertFalse(Parser.isValidTaskNumber("one", 3));
        assertFalse(Parser.isValidTaskNumber("1.5", 3));
        assertFalse(Parser.isValidTaskNumber("", 3));
        assertFalse(Parser.isValidTaskNumber("   ", 3));
    }

    @Test
    void parseTaskIndex_validTaskNumber_zeroBasedIndexReturned() {
        assertEquals(0, Parser.parseTaskIndex("1"));
        assertEquals(2, Parser.parseTaskIndex(" 3 "));
    }

    @Test
    void parseDate_validIsoDate_localDateReturned() throws CloverException {
        assertEquals(LocalDate.of(2024, 2, 29), Parser.parseDate("2024-02-29"));
    }

    @Test
    void parseDate_invalidDate_exceptionThrown() {
        CloverException exception = assertThrows(CloverException.class, () -> Parser.parseDate("2024-02-30"));

        assertEquals("Please enter dates in the format yyyy-MM-dd.", exception.getMessage());
    }
}
