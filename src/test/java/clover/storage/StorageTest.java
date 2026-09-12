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
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import clover.task.Deadline;
import clover.task.Event;
import clover.task.Task;
import clover.task.ToDo;
import clover.tutoree.Tutoree;

/** Tests loading task data from Clover's saved-data file. */
class StorageTest {
    @TempDir
    Path tempDir;

    @Test
    void save_allSupportedTaskTypes_tasksWrittenInCloverFileFormat() throws IOException {
        ToDo todo = new ToDo("read | annotate \\ draft");
        todo.markAsDone();
        Deadline deadline = new Deadline("submit report", LocalDate.of(2026, 9, 1));
        Event event = new Event("project meeting", LocalDate.of(2026, 9, 2), LocalDate.of(2026, 9, 3));
        event.markAsDone();
        Path dataFile = tempDir.resolve("data/clover.txt");

        new Storage(dataFile).save(java.util.List.of(todo, deadline, event));

        assertEquals(java.util.List.of(
                "T | 1 | read \\| annotate \\\\ draft",
                "D | 0 | submit report | 2026-09-01",
                "E | 1 | project meeting | 2026-09-02 | 2026-09-03"), Files.readAllLines(dataFile));
    }

    @Test
    void save_linkedTasksAndTutorees_recordsWrittenInExtendedFormats() throws IOException {
        Path taskFile = tempDir.resolve("data/clover.txt");
        Path tutoreeFile = tempDir.resolve("data/tutorees.txt");
        Storage storage = new Storage(taskFile, tutoreeFile);

        storage.save(List.of(
                new ToDo("prepare worksheet", "Alice Tan"),
                new Deadline("collect fee", LocalDate.of(2026, 9, 30), "Alice Tan"),
                new Event("lesson", LocalDate.of(2026, 9, 20), LocalDate.of(2026, 9, 20), "Alice Tan")));
        storage.saveTutorees(List.of(new Tutoree("Alice Tan", "12 Example Road", "$50/hour")));

        assertEquals(List.of(
                "T | 0 | prepare worksheet | Alice Tan",
                "D | 0 | collect fee | 2026-09-30 | Alice Tan",
                "E | 0 | lesson | 2026-09-20 | 2026-09-20 | Alice Tan"), Files.readAllLines(taskFile));
        assertEquals(List.of("S | Alice Tan | 12 Example Road | $50/hour"), Files.readAllLines(tutoreeFile));
    }

    @Test
    void load_linkedTaskAndTutoree_recordsRecreatedWithTheirDetails() throws IOException {
        Path taskFile = writeSavedData("D | 0 | collect fee | 2026-09-30 | Alice Tan");
        Path tutoreeFile = tempDir.resolve("tutorees.txt");
        Files.write(tutoreeFile, List.of("S | Alice Tan | 12 Example Road | $50/hour"));

        Storage storage = new Storage(taskFile, tutoreeFile);

        Deadline task = assertInstanceOf(Deadline.class, storage.load().getFirst());
        Tutoree tutoree = storage.loadTutorees().getFirst();
        assertEquals("Alice Tan", task.getTutoreeName());
        assertEquals("Alice Tan", tutoree.getName());
        assertEquals("12 Example Road", tutoree.getAddress());
        assertEquals("$50/hour", tutoree.getFee());
    }

    @Test
    void load_tutoreeDataContainsDuplicateNamesIgnoringCase_exceptionIdentifiesLine() throws IOException {
        Path taskFile = tempDir.resolve("clover.txt");
        Path tutoreeFile = tempDir.resolve("tutorees.txt");
        Files.write(tutoreeFile, List.of(
                "S | Alice Tan | 12 Example Road | $50/hour",
                "S | alice tan | 8 Sample Avenue | $55/hour"));

        IOException exception = assertThrows(IOException.class, () ->
                new Storage(taskFile, tutoreeFile).loadTutorees());

        assertEquals("Invalid tutoree data on line 2: duplicate tutoree name.", exception.getMessage());
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
    void load_dataFileContainsSupportedTaskTypes_tasksRecreatedWithTheirDetails() throws IOException {
        Path dataFile = writeSavedData(
                "T | 1 | read \\| annotate \\\\ draft",
                "D | 0 | submit report | 2026-09-01",
                "E | 1 | project meeting | 2026-09-02 | 2026-09-03");

        List<Task> tasks = new Storage(dataFile).load();

        assertEquals(3, tasks.size());
        assertInstanceOf(ToDo.class, tasks.get(0));
        assertEquals("read | annotate \\ draft", tasks.get(0).getDescription());
        assertTrue(tasks.get(0).isDone());

        Deadline deadline = assertInstanceOf(Deadline.class, tasks.get(1));
        assertEquals("submit report", deadline.getDescription());
        assertEquals(LocalDate.of(2026, 9, 1), deadline.getEndBy());
        assertFalse(deadline.isDone());

        Event event = assertInstanceOf(Event.class, tasks.get(2));
        assertEquals("project meeting", event.getDescription());
        assertEquals(LocalDate.of(2026, 9, 2), event.getStart());
        assertEquals(LocalDate.of(2026, 9, 3), event.getEnd());
        assertTrue(event.isDone());
    }

    @Test
    void load_dataFileContainsBlankLines_blankLinesIgnored() throws IOException {
        Path dataFile = writeSavedData("", "T | 0 | first task", "   ", "T | 0 | second task");

        List<Task> tasks = new Storage(dataFile).load();

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
