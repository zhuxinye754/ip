package clover.command;

import clover.exception.CloverException;
import clover.parser.Parser;
import clover.storage.Storage;
import clover.task.Event;
import clover.task.TaskList;
import clover.task.ToDo;
import clover.ui.Ui;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class TodoCommandTest {
    @TempDir
    Path tempDir;

    @Test
    public void inValidFormat_wrongFormat_exceptionThrown() throws CloverException {
        String command = "todo ";
        Command cmd = Parser.parse(command);
        Ui ui = new Ui();
        Storage storage = new Storage(tempDir.resolve("clover.txt"));
        TaskList taskList = new TaskList();
        CloverException exception = assertThrows(CloverException.class,
                () -> cmd.execute(taskList, ui, storage));

        assertEquals("The description of a todo cannot be empty.", exception.getMessage());

    }

    @Test
    public void validFormat_correctFormat_taskAdded() throws CloverException {
        String command = "todo do cooking";
        Command cmd = Parser.parse(command);
        Ui ui = new Ui();
        Storage storage = new Storage(tempDir.resolve("clover.txt"));
        TaskList taskList = new TaskList();
        cmd.execute(taskList, ui, storage);

        assertEquals(1, taskList.size());
        ToDo toDo = assertInstanceOf(ToDo.class, taskList.get(0));
        assertEquals("do cooking", toDo.getDescription());
        assertFalse(toDo.isDone());
    }
}
