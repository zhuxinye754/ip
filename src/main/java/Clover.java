import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;

public class Clover {
    /**
     * Prints a consistently formatted error message
     */
    private static void printError(String message) {
        System.out.println("____________________________________________________________");
        System.out.println(message);
        System.out.println("____________________________________________________________");
    }

    /**
     * Commands that Clover Chatbot accepts from the user.
     */
    public enum Command {
        LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, BYE
    }

    /**
     * Identifies the command at the start of the user's input.
     * Returns null when the input is a normal task description.
     */
    private static Command parseCommand(String input) {
        String firstWord = input.trim().split("\\s+", 2)[0].toUpperCase();

        try {
            return Command.valueOf(firstWord);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Checks whether text represents a task number already in the task list.
     */
    private static boolean isValidTaskNumber(String input, int taskCount) {
        try {
            int taskNumber = Integer.parseInt(input.trim());
            return taskNumber >= 1 && taskNumber <= taskCount;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** Saves tasks after a successful change, while keeping Clover usable if saving fails. */
    private static void saveTasks(Storage storage, ArrayList<Task> tasks) {
        try {
            storage.save(tasks);
        } catch (IOException e) {
            printError("I could not save your tasks to the data file.");
        }
    }

    public static void main(String[] args) {
        String banner = "  _____    _         ____    __      __   ______    _____\n"
                + " / ____|  | |       / __ \\   \\ \\    / /  |  ____|  |  __ \\\n"
                + "| |       | |      | |  | |   \\ \\  / /   | |__     | |__) |\n"
                + "| |       | |      | |  | |    \\ \\/ /    |  __|    |  _  /\n"
                + "| |____   | |____  | |  | |     \\  /     | |____   | | \\ \\\n"
                + " \\_____|  |______|  \\____/       \\/      |______|  |_|  \\_\\\n";
        Storage storage = new Storage();
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks;
        try {
            tasks = storage.load();
        } catch (IOException e) {
            printError("I could not load your saved tasks. Starting with an empty list.");
            tasks = new ArrayList<>();
        }
        System.out.println("____________________________________________________________");
        System.out.println(banner);

        System.out.println("Hello! I'm Clover.");
        System.out.println("What can I do for you?\n");
        System.out.println("____________________________________________________________");

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            try {
                if (input.isBlank()) {
                    throw new CloverException("Please enter a command or task description.");
                }

                Command command = parseCommand(input);

                if (command == null) {
                    tasks.add(new Task(input));
                    saveTasks(storage, tasks);
                    System.out.println(tasks.getLast());
                    continue;
                }

                // "list" command - outputs the list
                switch (command) {
                    case LIST:
                        System.out.println("""
                            ____________________________________________________________
                            Here are the tasks in your list:""");
                        for (int taskCount = 0; taskCount < tasks.size(); taskCount++) {
                            System.out.println((taskCount + 1)+ "." + tasks.get(taskCount));
                        }
                        System.out.println("____________________________________________________________");
                        break;
                    case MARK:
                        if (!isValidTaskNumber(input.substring(4), tasks.size())) {
                            throw new CloverException("Please enter a valid task number to mark.");
                        }
                        int index = Integer.parseInt(input.substring(5).trim()) - 1;
                        tasks.get(index).markAsDone();
                        saveTasks(storage, tasks);
                        System.out.println("""
                            ____________________________________________________________
                            Nice! I've marked this task as done:""");
                        System.out.println(" " + tasks.get(index));
                        System.out.println("____________________________________________________________");
                        break;
                    case UNMARK:
                        if (!isValidTaskNumber(input.substring(6), tasks.size())) {
                            throw new CloverException("Please enter a valid task number to unmark.");
                        }
                        int unmarkIndex = Integer.parseInt(input.substring(7).trim()) - 1;
                        tasks.get(unmarkIndex).markAsUndone();
                        saveTasks(storage, tasks);
                        System.out.println("""
                            ____________________________________________________________
                            OK, I've marked this task as not done yet:""");
                        System.out.println(" " + tasks.get(unmarkIndex));
                        System.out.println("____________________________________________________________");
                        break;
                    case TODO:
                        String description = input.substring(4).trim();
                        if (description.isEmpty()) {
                            throw new CloverException("The description of a todo cannot be empty.");
                        }
                        tasks.add(new ToDo(description));
                        saveTasks(storage, tasks);
                        System.out.println("""
                            ____________________________________________________________
                            Got it. I've added this task:""");
                        System.out.println(" " + tasks.getLast());
                        System.out.println("Now you have " + tasks.size() + " in the list.");
                        System.out.println("____________________________________________________________");
                        break;
                    case DEADLINE:
                        String target = "/by";
                        int targetIndex = input.indexOf(target);
                        if (targetIndex <= 9) {
                            throw new CloverException("Please use the format: deadline DESCRIPTION /by DUE DATE");
                        }
                        String endBy = input.substring(targetIndex + target.length()).trim();
                        String desc = input.substring(9, targetIndex).trim();
                        if (endBy.isEmpty()) {
                            throw new CloverException("Please use the format: deadline DESCRIPTION /by DUE DATE");
                        }
                        tasks.add(new Deadline(desc, endBy));
                        saveTasks(storage, tasks);
                        System.out.println("""
                            ____________________________________________________________
                            Got it. I've added this task:""");
                        System.out.println(" " + tasks.getLast());
                        System.out.println("Now you have " + tasks.size() + " in the list.");
                        System.out.println("____________________________________________________________");
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
                        tasks.add(new Event(eventDesc, fromDesc, toDesc));
                        saveTasks(storage, tasks);
                        System.out.println("""
                            ____________________________________________________________
                            Got it. I've added this task:""");
                        System.out.println(" " + tasks.getLast());
                        System.out.println("Now you have " + tasks.size() + " in the list.");
                        System.out.println("____________________________________________________________");
                        break;
                    case DELETE:
                        if (!isValidTaskNumber(input.substring(6), tasks.size())) {
                            throw new CloverException("Please enter a valid task number to delete.");
                        }

                        int delIndex = Integer.parseInt(input.substring(7).trim()) - 1;
                        Task deletedTask = tasks.remove(delIndex);
                        saveTasks(storage, tasks);
                        System.out.println("""
                                    ____________________________________________________________
                                    Noted. I've removed this task:""");
                        System.out.println(" " + deletedTask);
                        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                        System.out.println("____________________________________________________________");
                        break;
                    case BYE:
                        System.out.println("""
                                    Bye. Hope to see you again soon!
                                    ____________________________________________________________""");
                        return;
                    default:
                        if (input.isBlank()) {
                            throw new CloverException("Please enter a command or task description.");
                        }
                        System.out.println("____________________________________________________________");
                        Task t = new Task(input);
                        tasks.add(t);
                        saveTasks(storage, tasks);
                        System.out.println(tasks.getLast());
                        System.out.println("____________________________________________________________");
                }
            } catch (CloverException e) {
                printError(e.getMessage());
            }
        }
    }
}
