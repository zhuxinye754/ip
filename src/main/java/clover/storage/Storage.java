package clover.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import clover.task.Deadline;
import clover.task.Event;
import clover.task.Task;
import clover.task.ToDo;

/**
 * Saves Clover tasks to, and loads them from, a file on the hard disk.
 */
public class Storage {
    private static final Path FILE_PATH = Path.of("data", "clover.txt");
    private static final String TODO_TASK_TYPE = "T";
    private static final String DEADLINE_TASK_TYPE = "D";
    private static final String EVENT_TASK_TYPE = "E";
    private static final String INCOMPLETE_STATUS = "0";
    private static final String COMPLETE_STATUS = "1";
    private static final int TASK_TYPE_FIELD = 0;
    private static final int TASK_STATUS_FIELD = 1;
    private static final int TASK_DESCRIPTION_FIELD = 2;
    private static final int FIRST_DATE_FIELD = 3;
    private static final int SECOND_DATE_FIELD = 4;
    private final Path filePath;

    /**
     * Creates storage that uses Clover's default data-file location.
     */
    public Storage() {
        this(FILE_PATH);
    }

    /**
     * Creates storage that uses the supplied data-file location.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Writes the current task list to the data file.
     */
    public void save(List<Task> tasks) throws IOException {
        assert tasks != null : "Storage saves a task collection supplied by TaskList.";
        Files.createDirectories(filePath.getParent());
        if (Files.isDirectory(filePath)) {
            throw new IOException("The task data path is a directory.");
        }

        List<String> taskLines = new ArrayList<>();
        for (Task task : tasks) {
            assert task != null : "TaskList must not pass null tasks to storage.";
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

    /**
     * Loads saved tasks, or returns an empty list when Clover is run for the first time.
     */
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
    private String toFileLine(Task task) throws IOException {
        String status = task.isDone() ? COMPLETE_STATUS : INCOMPLETE_STATUS;
        if (task instanceof Deadline deadline) {
            return DEADLINE_TASK_TYPE + " | " + status + " | " + escape(deadline.getDescription())
                    + " | " + escape(deadline.getEndBy().toString());
        }
        if (task instanceof Event event) {
            return EVENT_TASK_TYPE + " | " + status + " | " + escape(event.getDescription())
                    + " | " + escape(event.getStart().toString())
                    + " | " + escape(event.getEnd().toString());
        }
        if (task instanceof ToDo) {
            return TODO_TASK_TYPE + " | " + status + " | " + escape(task.getDescription());
        }
        throw new IOException("Unsupported task type.");
    }

    /** Parses an ISO date stored in the data file. */
    private LocalDate parseDate(String text, int lineNumber) throws IOException {
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException exception) {
            throw invalidData(lineNumber, "invalid date");
        }
    }

    /** Recreates one task from a pipe-separated file line. */
    private Task fromFileLine(String line, int lineNumber) throws IOException {
        List<String> parts = splitFields(line, lineNumber);
        assert !parts.isEmpty() : "Splitting a task-data line always produces its first field.";

        Task task = switch (parts.get(TASK_TYPE_FIELD)) {
            case TODO_TASK_TYPE -> {
                requirePartCount(parts, 3, lineNumber);
                yield new ToDo(parts.get(TASK_DESCRIPTION_FIELD));
            }
            case DEADLINE_TASK_TYPE -> {
                requirePartCount(parts, 4, lineNumber);
                yield new Deadline(parts.get(TASK_DESCRIPTION_FIELD),
                        parseDate(parts.get(FIRST_DATE_FIELD), lineNumber));
            }
            case EVENT_TASK_TYPE -> {
                requirePartCount(parts, 5, lineNumber);
                yield new Event(parts.get(TASK_DESCRIPTION_FIELD),
                        parseDate(parts.get(FIRST_DATE_FIELD), lineNumber),
                        parseDate(parts.get(SECOND_DATE_FIELD), lineNumber));
            }
            default -> throw invalidData(lineNumber, "unknown task type");
        };

        if (COMPLETE_STATUS.equals(parts.get(TASK_STATUS_FIELD))) {
            task.markAsDone();
        } else if (!INCOMPLETE_STATUS.equals(parts.get(TASK_STATUS_FIELD))) {
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
        } catch (AtomicMoveNotSupportedException exception) {
            Files.move(temporaryFile, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /** Verifies that a task line includes all fields required by its task type. */
    private void requirePartCount(List<String> parts, int expectedCount, int lineNumber)
            throws IOException {
        if (parts.size() != expectedCount) {
            throw invalidData(lineNumber, "wrong number of fields");
        }
    }

    /** Creates a clear error for a corrupt line in the saved task data. */
    private IOException invalidData(int lineNumber, String reason) {
        return new IOException("Invalid task data on line " + lineNumber + ": " + reason + ".");
    }
}
