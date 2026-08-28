import java.util.ArrayList;
import java.io.IOException;

public class Clover {
    /** Saves tasks after a successful change, while keeping Clover usable if saving fails. */

    private final Storage storage;
    private final Ui ui;
    private TaskList tasks;

    public Clover() {
        storage = new Storage();
        ui = new Ui();

        try {
            tasks = new TaskList(storage.load());
        } catch (IOException | SecurityException e) {
            ui.showError("I could not load your saved tasks. Starting with an empty list.");
            tasks = new TaskList();
        }
    }
    private void saveTasks() {
        try {
            storage.save(tasks.asList());
        } catch (IOException | SecurityException e) {
            ui.showError("I could not save your tasks to the data file.");
        }
    }

    public  void run() {
        ui.showWelcome();

        while (ui.hasNextCommand()) {
            String input = ui.readCommand();
            try {
                if (input.isBlank()) {
                    throw new CloverException("Please enter a command or task description.");
                }

                Parser.Command command = Parser.parseCommand(input);

                if (command == null) {
                    tasks.add(new Task(input));
                    saveTasks();
                    ui.showPlainTask(tasks.getLast());
                    continue;
                }

                // "list" command - outputs the list
                switch (command) {
                    case LIST:
                        ui.showTaskList(tasks.asList());
                        break;
                    case MARK:
                        if (!Parser.isValidTaskNumber(input.substring(4), tasks.size())) {
                            throw new CloverException("Please enter a valid task number to mark.");
                        }
                        int index = Parser.parseTaskIndex(input.substring(4));
                        tasks.get(index).markAsDone();
                        saveTasks();
                        ui.showTaskMarked(tasks.get(index));
                        break;
                    case UNMARK:
                        if (!Parser.isValidTaskNumber(input.substring(6), tasks.size())) {
                            throw new CloverException("Please enter a valid task number to unmark.");
                        }
                        int unmarkIndex = Parser.parseTaskIndex(input.substring(6));
                        tasks.get(unmarkIndex).markAsUndone();
                        saveTasks();
                        ui.showTaskUnmarked(tasks.get(unmarkIndex));
                        break;
                    case TODO:
                        String description = input.substring(4).trim();
                        if (description.isEmpty()) {
                            throw new CloverException("The description of a todo cannot be empty.");
                        }
                        tasks.add(new ToDo(description));
                        saveTasks();
                        ui.showTaskAdded(tasks.getLast(), tasks.size());
                        break;
                    case DEADLINE:
                        String target = "/by";
                        int targetIndex = input.indexOf(target);
                        if (targetIndex <= 9) {
                            throw new CloverException("Please use the format: deadline DESCRIPTION /by DUE DATE");
                        }
                        String endBy = input.substring(targetIndex + target.length()).trim();
                        if (endBy.isEmpty()) {
                            throw new CloverException("Please use the format: deadline DESCRIPTION /by DUE DATE");
                        }
                        var date = Parser.parseDate(endBy);
                        String desc = input.substring(9, targetIndex).trim();
                        tasks.add(new Deadline(desc, date));
                        saveTasks();
                        ui.showTaskAdded(tasks.getLast(), tasks.size());
                        break;
                    case EVENT:
                        int fromIndex = input.indexOf("/from");
                        int toIndex = input.indexOf("/to");
                        if (fromIndex <= 6 || toIndex <= fromIndex + 5) {
                            throw new CloverException("Please use the format: event DESCRIPTION /from START /to END");
                        }
                        String fromDesc = input.substring(fromIndex + 5, toIndex).trim();
                        String toDesc = input.substring(toIndex + 3).trim();
                        String eventDesc = input.substring(6, fromIndex).trim();
                        if (fromDesc.isEmpty() || toDesc.isEmpty()) {
                            throw new CloverException("Please use the format: event DESCRIPTION /from START /to END");
                        }
                        var fromDate = Parser.parseDate(fromDesc);
                        var toDate = Parser.parseDate(toDesc);
                        tasks.add(new Event(eventDesc, fromDate, toDate));
                        saveTasks();
                        ui.showTaskAdded(tasks.getLast(), tasks.size());
                        break;
                    case DELETE:
                        if (!Parser.isValidTaskNumber(input.substring(6), tasks.size())) {
                            throw new CloverException("Please enter a valid task number to delete.");
                        }

                        int delIndex = Parser.parseTaskIndex(input.substring(6));
                        Task deletedTask = tasks.remove(delIndex);
                        saveTasks();
                        ui.showTaskDeleted(deletedTask, tasks.size());
                        break;
                    case BYE:
                        ui.showGoodbye();
                        ui.close();
                        return;
                }
            } catch (CloverException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new Clover().run();
    }
}
