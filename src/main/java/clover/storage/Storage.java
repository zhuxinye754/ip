package clover.storage;

import clover.task.Deadline;
import clover.task.Event;
import clover.task.Task;
import clover.task.ToDo;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/** Saves Clover tasks to, and loads them from, a file on the hard disk. */
public class Storage {
    private static final Path FILE_PATH = Path.of("data", "clover.txt");
    private final Path filePath;

    /** Creates storage using Clover's default data-file location. */
    public Storage() {
        this(FILE_PATH);
    }

    /** Creates storage that reads from and writes to the given data file. */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /** Writes the current task list to the data file. */
    public void save(List<Task> tasks) throws IOException {
        Files.createDirectories(filePath.getParent());
        if (Files.isDirectory(filePath)) {
            throw new IOException("The task data path is a directory.");
        }

        List<String> taskLines = new ArrayList<>();
        for (Task task : tasks) {
            taskLines.add(toFileLine(task));
        }

        Path temporaryFile = Files.createTempFile(filePath.getParent(), "clover-", ".tmp");
        try {
            Files.write(temporaryFile, taskLines, StandardCharsets.UTF_8);
            moveIntoPlace(temporaryFile);
        } finally {
            Files.deleteIfExists(temporaryFile);
        }
    }

    /** Loads saved tasks, or returns an empty list when Clover is run for the first time. */
    public ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        if (Files.notExists(filePath)) {
            return tasks;
        }
        if (!Files.isRegularFile(filePath)) {
            throw new IOException("The task data path is not a regular file.");
        }

        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        for (int lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);
            if (!line.isBlank()) {
                tasks.add(fromFileLine(line, lineNumber + 1));
            }
        }
        return tasks;
    }

    /** Converts one task to a stable, pipe-separated file line. */
    private String toFileLine(Task task) {
        String completed = task.isDone() ? "1" : "0";
        if (task instanceof Deadline deadline) {
            return "D | " + completed + " | " + escape(deadline.getDescription())
                    + " | " + escape(deadline.getEndBy().toString());
        }
        if (task instanceof Event event) {
            return "E | " + completed + " | " + escape(event.getDescription())
                    + " | " + escape(event.getStart().toString()) + " | " + escape(event.getEnd().toString());
        }
        if (task instanceof ToDo) {
            return "T | " + completed + " | " + escape(task.getDescription());
        }
        return "N | " + completed + " | " + escape(task.getDescription());
    }

    /**
     * Parses an ISO date stored in the data file.
     */
    private LocalDate parseDate(String text, int lineNumber) throws IOException {
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException e) {
            throw invalidData(lineNumber, "invalid date");
        }
    }

    /** Recreates one task from a pipe-separated file line. */
    private Task fromFileLine(String line, int lineNumber) throws IOException {
        List<String> parts = splitFields(line, lineNumber);

        Task task;
        switch (parts.get(0)) {
            case "N":
                requirePartCount(parts, 3, lineNumber);
                task = new Task(parts.get(2));
                break;
            case "T":
                requirePartCount(parts, 3, lineNumber);
                task = new ToDo(parts.get(2));
                break;
            case "D":
                requirePartCount(parts, 4, lineNumber);
                task = new Deadline(parts.get(2), parseDate(parts.get(3), lineNumber));
                break;
            case "E":
                requirePartCount(parts, 5, lineNumber);
                task = new Event(parts.get(2),
                        parseDate(parts.get(3), lineNumber),
                        parseDate(parts.get(4), lineNumber));
                break;
            default:
                throw invalidData(lineNumber, "unknown task type");
        }

        if ("1".equals(parts.get(1))) {
            task.markAsDone();
        } else if (!"0".equals(parts.get(1))) {
            throw invalidData(lineNumber, "invalid task status");
        }
        return task;
    }

    /** Splits a line at unescaped pipe characters and removes delimiter spacing. */
    private List<String> splitFields(String line, int lineNumber) throws IOException {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean isEscaped = false;
        for (char character : line.toCharArray()) {
            if (isEscaped) {
                field.append(character);
                isEscaped = false;
            } else if (character == '\\') {
                isEscaped = true;
            } else if (character == '|') {
                fields.add(field.toString().trim());
                field.setLength(0);
            } else {
                field.append(character);
            }
        }
        if (isEscaped) {
            throw invalidData(lineNumber, "unfinished escape sequence");
        }
        fields.add(field.toString().trim());
        return fields;
    }

    /** Escapes characters that have a special meaning in the file format. */
    private String escape(String text) {
        return text.replace("\\", "\\\\").replace("|", "\\|");
    }

    /** Replaces the old data file only after the temporary file is fully written. */
    private void moveIntoPlace(Path temporaryFile) throws IOException {
        try {
            Files.move(temporaryFile, filePath, StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(temporaryFile, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /** Verifies that a task line includes all fields required by its task type. */
    private void requirePartCount(List<String> parts, int expectedCount, int lineNumber) throws IOException {
        if (parts.size() != expectedCount) {
            throw invalidData(lineNumber, "wrong number of fields");
        }
    }

    /** Creates a clear error for a corrupt line in the saved task data. */
    private IOException invalidData(int lineNumber, String reason) {
        return new IOException("Invalid task data on line " + lineNumber + ": " + reason + ".");
    }
}
