import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Saves Clover tasks to, and loads them from, a file on the hard disk. */
public class Storage {
    private static final Path FILE_PATH = Path.of("data", "duke.txt");

    /** Writes the current task list to the data file. */
    public void save(List<Task> tasks) throws IOException {
        Files.createDirectories(FILE_PATH.getParent());
        List<String> taskLines = new ArrayList<>();
        for (Task task : tasks) {
            taskLines.add(toFileLine(task));
        }
        Files.write(FILE_PATH, taskLines, StandardCharsets.UTF_8);
    }

    /** Loads saved tasks, or returns an empty list when Clover is run for the first time. */
    public ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(FILE_PATH)) {
            return tasks;
        }

        for (String line : Files.readAllLines(FILE_PATH, StandardCharsets.UTF_8)) {
            tasks.add(fromFileLine(line));
        }
        return tasks;
    }

    /** Converts one task to a stable, pipe-separated file line. */
    private String toFileLine(Task task) {
        String completed = task.isDone() ? "1" : "0";
        if (task instanceof Deadline deadline) {
            return "D | " + completed + " | " + deadline.getDescription()
                    + " | " + deadline.getEndBy();
        }
        if (task instanceof Event event) {
            return "E | " + completed + " | " + event.getDescription()
                    + " | " + event.getStart() + " | " + event.getEnd();
        }
        if (task instanceof ToDo) {
            return "T | " + completed + " | " + task.getDescription();
        }
        return "N | " + completed + " | " + task.getDescription();
    }

    /** Recreates one task from a pipe-separated file line. */
    private Task fromFileLine(String line) throws IOException {
        String[] parts = line.split(" \\| ", -1);
        if (parts.length < 3) {
            throw new IOException("Invalid task data: " + line);
        }

        Task task;
        switch (parts[0]) {
        case "N":
            task = new Task(parts[2]);
            break;
        case "T":
            task = new ToDo(parts[2]);
            break;
        case "D":
            requirePartCount(parts, 4, line);
            task = new Deadline(parts[2], parts[3]);
            break;
        case "E":
            requirePartCount(parts, 5, line);
            task = new Event(parts[2], parts[3], parts[4]);
            break;
        default:
            throw new IOException("Unknown task type in data: " + line);
        }

        if ("1".equals(parts[1])) {
            task.markAsDone();
        } else if (!"0".equals(parts[1])) {
            throw new IOException("Invalid task status in data: " + line);
        }
        return task;
    }

    /** Verifies that a task line includes all fields required by its task type. */
    private void requirePartCount(String[] parts, int expectedCount, String line) throws IOException {
        if (parts.length != expectedCount) {
            throw new IOException("Invalid task data: " + line);
        }
    }
}
