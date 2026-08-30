package clover.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import clover.task.Deadline;
import clover.task.Event;
import clover.task.Task;
import clover.task.ToDo;

/** Tests loading task data from Clover's saved-data file. */
class StorageTest {
    @TempDir
    Path tempDir;

    @Test
    void save_allTaskTypes_tasksWrittenInCloverFileFormat() throws IOException {
        Task plainTask = new Task("plain task");
        ToDo todo = new ToDo("read | annotate \\ draft");
        todo.markAsDone();
        Deadline deadline = new Deadline("submit report", LocalDate.of(2026, 9, 1));
        Event event = new Event("project meeting", LocalDate.of(2026, 9, 2), LocalDate.of(2026, 9, 3));
        event.markAsDone();
        Path dataFile = tempDir.resolve("data/clover.txt");

        new Storage(dataFile).save(java.util.List.of(plainTask, todo, deadline, event));

        assertEquals(java.util.List.of(
                "N | 0 | plain task",
                "T | 1 | read \\| annotate \\\\ draft",
                "D | 0 | submit report | 2026-09-01",
                "E | 1 | project meeting | 2026-09-02 | 2026-09-03"), Files.readAllLines(dataFile));
    }

    @Test
    void save_dataPathIsDirectory_exceptionThrown() throws IOException {
        Path directory = tempDir.resolve("tasks");
        Files.createDirectory(directory);

        IOException exception = assertThrows(IOException.class, () -> new Storage(directory).save(java.util.List.of()));

        assertEquals("The task data path is a directory.", exception.getMessage());
    }

    @Test
    void load_dataFileDoesNotExist_emptyTaskListReturned() throws IOException {
        Storage storage = new Storage(tempDir.resolve("clover.txt"));

        assertTrue(storage.load().isEmpty());
    }

    @Test
    void load_dataFileContainsAllTaskTypes_tasksRecreatedWithTheirDetails() throws IOException {
        Path dataFile = writeSavedData(
                "N | 0 | plain task",
                "T | 1 | read \\| annotate \\\\ draft",
                "D | 0 | submit report | 2026-09-01",
                "E | 1 | project meeting | 2026-09-02 | 2026-09-03");

        ArrayList<Task> tasks = new Storage(dataFile).load();

        assertEquals(4, tasks.size());
        assertEquals(Task.class, tasks.get(0).getClass());
        assertEquals("plain task", tasks.get(0).getDescription());
        assertFalse(tasks.get(0).isDone());

        assertInstanceOf(ToDo.class, tasks.get(1));
        assertEquals("read | annotate \\ draft", tasks.get(1).getDescription());
        assertTrue(tasks.get(1).isDone());

        Deadline deadline = assertInstanceOf(Deadline.class, tasks.get(2));
        assertEquals("submit report", deadline.getDescription());
        assertEquals(LocalDate.of(2026, 9, 1), deadline.getEndBy());
        assertFalse(deadline.isDone());

        Event event = assertInstanceOf(Event.class, tasks.get(3));
        assertEquals("project meeting", event.getDescription());
        assertEquals(LocalDate.of(2026, 9, 2), event.getStart());
        assertEquals(LocalDate.of(2026, 9, 3), event.getEnd());
        assertTrue(event.isDone());
    }

    @Test
    void load_dataFileContainsBlankLines_blankLinesIgnored() throws IOException {
        Path dataFile = writeSavedData("", "N | 0 | first task", "   ", "T | 0 | second task");

        ArrayList<Task> tasks = new Storage(dataFile).load();

        assertEquals(2, tasks.size());
        assertEquals("first task", tasks.get(0).getDescription());
        assertEquals("second task", tasks.get(1).getDescription());
    }

    @Test
    void load_dataPathIsDirectory_exceptionThrown() throws IOException {
        Path directory = tempDir.resolve("tasks");
        Files.createDirectory(directory);

        IOException exception = assertThrows(IOException.class, () -> new Storage(directory).load());

        assertEquals("The task data path is not a regular file.", exception.getMessage());
    }

    @Test
    void load_dataContainsUnknownTaskType_exceptionIdentifiesLine() throws IOException {
        Path dataFile = writeSavedData("Q | 0 | unknown task");

        IOException exception = assertThrows(IOException.class, () -> new Storage(dataFile).load());

        assertEquals("Invalid task data on line 1: unknown task type.", exception.getMessage());
    }

    @Test
    void load_dataContainsInvalidStatus_exceptionIdentifiesLine() throws IOException {
        Path dataFile = writeSavedData("T | done | read book");

        IOException exception = assertThrows(IOException.class, () -> new Storage(dataFile).load());

        assertEquals("Invalid task data on line 1: invalid task status.", exception.getMessage());
    }

    @Test
    void load_dataContainsWrongFieldCount_exceptionIdentifiesLine() throws IOException {
        Path dataFile = writeSavedData("D | 0 | submit report");

        IOException exception = assertThrows(IOException.class, () -> new Storage(dataFile).load());

        assertEquals("Invalid task data on line 1: wrong number of fields.", exception.getMessage());
    }

    @Test
    void load_dataContainsInvalidDate_exceptionIdentifiesLine() throws IOException {
        Path dataFile = writeSavedData("E | 0 | project meeting | tomorrow | 2026-09-03");

        IOException exception = assertThrows(IOException.class, () -> new Storage(dataFile).load());

        assertEquals("Invalid task data on line 1: invalid date.", exception.getMessage());
    }

    @Test
    void load_dataContainsUnfinishedEscapeSequence_exceptionIdentifiesLine() throws IOException {
        Path dataFile = writeSavedData("T | 0 | incomplete escape\\");

        IOException exception = assertThrows(IOException.class, () -> new Storage(dataFile).load());

        assertEquals("Invalid task data on line 1: unfinished escape sequence.", exception.getMessage());
    }

    private Path writeSavedData(String... lines) throws IOException {
        Path dataFile = tempDir.resolve("clover.txt");
        Files.write(dataFile, java.util.List.of(lines));
        return dataFile;
    }
}
