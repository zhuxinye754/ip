package clover.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import clover.exception.CloverException;
import clover.storage.Storage;
import clover.task.Task;
import clover.task.TaskList;
import clover.ui.Ui;

/** Tests marking tasks complete through the mark command. */
class MarkCommandTest {
    @TempDir
    Path tempDir;

    @Test
    void execute_validTaskNumber_taskMarkedAndSaved() throws CloverException, java.io.IOException {
        Task task = new Task("read book");
        TaskList tasks = new TaskList();
        tasks.add(task);
        Storage storage = new Storage(tempDir.resolve("clover.txt"));

        new MarkCommand("1").execute(tasks, new Ui(), storage);

        assertTrue(task.isDone());
        assertTrue(storage.load().getFirst().isDone());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "0", "2", "one"})
    void execute_invalidTaskNumber_exceptionThrownWithoutChangingTask(String taskNumber) {
        Task task = new Task("read book");
        TaskList tasks = new TaskList();
        tasks.add(task);

        CloverException exception = assertThrows(CloverException.class, () -> new MarkCommand(taskNumber).execute(
                tasks, new Ui(), new Storage(tempDir.resolve("clover.txt"))));

        assertEquals("Please enter a valid task number to mark.", exception.getMessage());
        assertFalse(task.isDone());
    }
}
