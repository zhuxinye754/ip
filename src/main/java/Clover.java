import java.util.ArrayList;
import java.util.Scanner;

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

    public static void main(String[] args) {
        String banner = "  _____    _         ____    __      __   ______    _____\n"
                + " / ____|  | |       / __ \\   \\ \\    / /  |  ____|  |  __ \\\n"
                + "| |       | |      | |  | |   \\ \\  / /   | |__     | |__) |\n"
                + "| |       | |      | |  | |    \\ \\/ /    |  __|    |  _  /\n"
                + "| |____   | |____  | |  | |     \\  /     | |____   | | \\ \\\n"
                + " \\_____|  |______|  \\____/       \\/      |______|  |_|  \\_\\\n";
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();
        System.out.println("____________________________________________________________");
        System.out.println(banner);

        System.out.println("Hello! I'm Clover.");
        System.out.println("What can I do for you?\n");
        System.out.println("____________________________________________________________");

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            try {
                // "list" command - outputs the list
                if (input.equals("list")) {
                    System.out.println("""
                            ____________________________________________________________
                            Here are the tasks in your list:""");
                    for (int taskCount = 0; taskCount < tasks.size(); taskCount++) {
                        System.out.println((taskCount + 1)+ "." + tasks.get(taskCount));
                    }
                    System.out.println("____________________________________________________________");
                } else if (input.equals("unmark") || input.startsWith("unmark ")) {
                    if (!isValidTaskNumber(input.substring(6), tasks.size())) {
                        throw new CloverException("Please enter a valid task number to unmark.");
                    }
                    int index = Integer.parseInt(input.substring(7).trim()) - 1;
                    tasks.get(index).markAsUndone();
                    System.out.println("""
                            ____________________________________________________________
                            OK, I've marked this task as not done yet:""");
                    System.out.println(" r" + tasks.get(index));
                    System.out.println("____________________________________________________________");
                } else if (input.equals("mark") || input.startsWith("mark ")) {
                    if (!isValidTaskNumber(input.substring(4), tasks.size())) {
                        throw new CloverException("Please enter a valid task number to mark.");
                    }
                    int index = Integer.parseInt(input.substring(5).trim()) - 1;
                    tasks.get(index).markAsDone();
                    System.out.println("""
                            ____________________________________________________________
                            Nice! I've marked this task as done:""");
                    System.out.println(" " + tasks.get(index));
                    System.out.println("____________________________________________________________");
                } else if (input.equals("todo") || input.startsWith("todo ")) {
                    String description = input.substring(4).trim();
                    if (description.isEmpty()) {
                        throw new CloverException("The description of a todo cannot be empty.");
                    }
                    tasks.add(new ToDo(description));
                    System.out.println("""
                            ____________________________________________________________
                            Got it. I've added this task:""");
                    System.out.println(" " + tasks.getLast());
                    System.out.println("Now you have " + tasks.size() + " in the list.");
                    System.out.println("____________________________________________________________");
                } else if (input.equals("deadline") || input.startsWith("deadline ")) {
                    String target = "/by";
                    int index = input.indexOf(target);
                    if (index <= 9) {
                        throw new CloverException("Please use the format: deadline DESCRIPTION /by DUE DATE");
                    }
                    String endBy = input.substring(index + target.length()).trim();
                    String desc = input.substring(9, index).trim();
                    if (endBy.isEmpty()) {
                        throw new CloverException("Please use the format: deadline DESCRIPTION /by DUE DATE");
                    }
                    tasks.add(new Deadline(desc, endBy));
                    System.out.println("""
                            ____________________________________________________________
                            Got it. I've added this task:""");
                    System.out.println(" " + tasks.getLast());
                    System.out.println("Now you have " + tasks.size() + " in the list.");
                    System.out.println("____________________________________________________________");
                } else if (input.equals("event") || input.startsWith("event ")) {
                    int fromIndex = input.indexOf("/from");
                    int toIndex = input.indexOf("/to");
                    if (fromIndex <= 6 || toIndex <= fromIndex + 5) {
                        throw new CloverException("Please use the format: event DESCRIPTION /from START /to END");
                    }
                    String fromDesc = input.substring(fromIndex + 5, toIndex).trim();
                    String toDesc = input.substring(toIndex + 3).trim();
                    String desc = input.substring(6, fromIndex).trim();
                    if (fromDesc.isEmpty() || toDesc.isEmpty()) {
                        throw new CloverException("Please use the format: event DESCRIPTION /from START /to END");
                    }
                    tasks.add(new Event(desc, fromDesc, toDesc));
                    System.out.println("""
                            ____________________________________________________________
                            Got it. I've added this task:""");
                    System.out.println(" " + tasks.getLast());
                    System.out.println("Now you have " + tasks.size() + " in the list.");
                    System.out.println("____________________________________________________________");
                } else if (input.equals("bye")) { // "bye" command - exits program
                    System.out.println("""
                                    Bye. Hope to see you again soon!
                                    ____________________________________________________________""");
                    break;
                } else { // add to list
                    if (input.isBlank()) {
                        throw new CloverException("Please enter a command or task description.");
                    }
                    System.out.println("____________________________________________________________");
                    Task t = new Task(input);
                    tasks.add(t);
                    System.out.println(tasks.getLast());
                    System.out.println("____________________________________________________________");
                }
            } catch (CloverException e) {
                printError(e.getMessage());
            }
        }
    }
}
