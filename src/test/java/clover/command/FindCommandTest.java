package clover.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import clover.exception.CloverException;
import clover.storage.Storage;
import clover.task.Deadline;
import clover.task.TaskList;
import clover.task.ToDo;
import clover.ui.Ui;

/** Tests searching tasks by a keyword in their descriptions. */
class FindCommandTest {
    @TempDir
    Path tempDir;

    @Test
    void execute_caseInsensitiveMatchingTasks_displaysResultsInTaskOrder() throws CloverException {
        TaskList taskList = new TaskList();
        taskList.add(new ToDo("read book"));
        taskList.add(new Deadline("return BOOK", LocalDate.of(2026, 6, 6)));
        taskList.add(new ToDo("buy groceries"));

        String output = captureExecutionOutput(new FindCommand("BoOk"), taskList);

        assertEquals("Here are the matching tasks in your list:\n"
                + "1.[T] [ ] read book\n"
                + "2.[D] [ ] return BOOK (by: Jun 6 2026)\n", output);
    }

    @Test
    void constructor_blankKeyword_exceptionThrown() {
        CloverException exception = assertThrows(CloverException.class, () -> new FindCommand("   "));

        assertEquals("Please enter a keyword to search for.", exception.getMessage());
    }

    /** Executes a command while capturing the console output it sends through the UI. */
    private String captureExecutionOutput(FindCommand command, TaskList taskList) throws CloverException {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            command.execute(taskList, new Ui(), new Storage(tempDir.resolve("clover.txt")));
        } finally {
            System.setOut(originalOut);
        }
        return output.toString(StandardCharsets.UTF_8);
    }
}
