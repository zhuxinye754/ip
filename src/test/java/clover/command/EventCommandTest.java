package clover.command;

import clover.exception.CloverException;
import clover.parser.Parser;
import clover.storage.Storage;
import clover.task.Event;
import clover.task.Task;
import clover.task.TaskList;
import clover.ui.Ui;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Tests validation and execution of event commands. */
class EventCommandTest {
    @TempDir
    Path tempDir;

    @ParameterizedTest
    @ValueSource(strings = {
            "event do homework",
            "event do art /from /to 2021-12-21",
            "event do coding /from 2021-12-21 /to",
            "event /from 2021-12-21 /to 2022-12-21"
    })
    void execute_invalidStructure_exceptionThrownWithoutAddingTask(String command) throws CloverException {

        Command cmd = Parser.parse(command);
        Ui ui = new Ui();
        Storage storage = new Storage(tempDir.resolve("clover.txt"));
        TaskList taskList = new TaskList();
        CloverException exception = assertThrows(CloverException.class,
                () -> cmd.execute(taskList, ui, storage));

        assertEquals("Please use the format: event DESCRIPTION /from START /to END", exception.getMessage());
        assertTrue(taskList.asList().isEmpty());
    }

    @Test
    void execute_validEvent_taskAddedAndSaved() throws IOException, CloverException {
        String command = "event do cooking /from 2021-12-21 /to 2022-12-21";
        Command cmd = Parser.parse(command);
        Ui ui = new Ui();
        Storage storage = new Storage(tempDir.resolve("clover.txt"));
        TaskList taskList = new TaskList();
        cmd.execute(taskList, ui, storage);

        assertEquals(1, taskList.size());
        Event event = assertInstanceOf(Event.class, taskList.get(0));
        assertEquals("do cooking", event.getDescription());
        assertEquals(LocalDate.of(2021, 12, 21), event.getStart());
        assertEquals(LocalDate.of(2022, 12, 21), event.getEnd());
        assertFalse(event.isDone());

        ArrayList<Task> savedTasks = storage.load();
        Event savedEvent = assertInstanceOf(Event.class, savedTasks.getFirst());
        assertEquals(event.getDescription(), savedEvent.getDescription());
        assertEquals(event.getStart(), savedEvent.getStart());
        assertEquals(event.getEnd(), savedEvent.getEnd());
    }

    @Test
    void execute_invalidStartDate_exceptionThrownWithoutAddingTask() throws CloverException {
        String command = "event do cooking /from today /to 2022-12-21";
        Command cmd = Parser.parse(command);
        Ui ui = new Ui();
        Storage storage = new Storage(tempDir.resolve("clover.txt"));
        TaskList taskList = new TaskList();
        CloverException exception = assertThrows(CloverException.class,
                () -> cmd.execute(taskList, ui, storage));

        assertEquals("Please enter dates in the format yyyy-MM-dd.", exception.getMessage());
        assertTrue(taskList.asList().isEmpty());
    }

    @Test
    void execute_invalidEndDate_exceptionThrownWithoutAddingTask() throws CloverException {
        String command = "event do cooking /from 2021-12-21 /to tomorrow";
        Command cmd = Parser.parse(command);
        Ui ui = new Ui();
        Storage storage = new Storage(tempDir.resolve("clover.txt"));
        TaskList taskList = new TaskList();
        CloverException exception = assertThrows(CloverException.class,
                () -> cmd.execute(taskList, ui, storage));

        assertEquals("Please enter dates in the format yyyy-MM-dd.", exception.getMessage());
        assertTrue(taskList.asList().isEmpty());
    }
}
