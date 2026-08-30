package clover.command;

import clover.exception.CloverException;
import clover.parser.Parser;
import clover.storage.Storage;
import clover.task.Deadline;
import clover.task.TaskList;
import clover.ui.Ui;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class DeadlineCommandTest {
    @TempDir
    Path tempDir;

    @ParameterizedTest
    @ValueSource(strings = {
            "deadline do homework",
            "deadline do art /by",
            "deadline /by 2021-10-21",
    })
    public void inValidFormat_wrongFormat_exceptionThrown(String command) throws CloverException {
        Command cmd = Parser.parse(command);
        Ui ui = new Ui();
        Storage storage = new Storage(tempDir.resolve("clover.txt"));
        TaskList taskList = new TaskList();
        CloverException exception = assertThrows(CloverException.class,
                () -> cmd.execute(taskList, ui, storage));

        assertEquals("Please use the format: deadline DESCRIPTION /by DUE DATE", exception.getMessage());

    }

    @Test
    public void validFormat_correctFormat_taskAdded() throws CloverException {
        String command = "deadline do cooking /by 2021-12-21";
        Command cmd = Parser.parse(command);
        Ui ui = new Ui();
        Storage storage = new Storage(tempDir.resolve("clover.txt"));
        TaskList taskList = new TaskList();
        cmd.execute(taskList, ui, storage);

        assertEquals(1, taskList.size());
        Deadline deadline = assertInstanceOf(Deadline.class, taskList.get(0));
        assertEquals("do cooking", deadline.getDescription());
        assertEquals(LocalDate.of(2021, 12, 21), deadline.getEndBy());
        assertFalse(deadline.isDone());
    }

    @Test
    public void inValidFormat_invalidDate_exceptionThrown() throws CloverException {
        String command = "deadline do art /by tomorrow ";
        Command cmd = Parser.parse(command);
        Ui ui = new Ui();
        Storage storage = new Storage(tempDir.resolve("clover.txt"));
        TaskList taskList = new TaskList();
        CloverException exception = assertThrows(CloverException.class,
                () -> cmd.execute(taskList, ui, storage));

        assertEquals("Please enter dates in the format yyyy-MM-dd.", exception.getMessage());

    }
}
