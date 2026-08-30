package clover.ui;

import java.util.List;
import java.util.Scanner;

import clover.task.Task;

/**
 * Handles Clover's console input and output.
 */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";

    private final Scanner scanner;

    /**
     * Creates a UI that reads commands from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Returns whether another command can be read from standard input.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Returns the next command entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays Clover's welcome banner and greeting.
     */
    public void showWelcome() {
        String banner = "  _____    _         ____    __      __   ______    _____\n"
                + " / ____|  | |       / __ \\   \\ \\    / /  |  ____|  |  __ \\\n"
                + "| |       | |      | |  | |   \\ \\  / /   | |__     | |__) |\n"
                + "| |       | |      | |  | |    \\ \\/ /    |  __|    |  _  /\n"
                + "| |____   | |____  | |  | |     \\  /     | |____   | | \\ \\\n"
                + " \\_____|  |______|  \\____/       \\/      |______|  |_|  \\_\\\n";
        System.out.println(DIVIDER);
        System.out.println(banner);
        System.out.println("Hello! I'm Clover.");
        System.out.println("What can I do for you?\n");
        System.out.println(DIVIDER);
    }

    /**
     * Displays the standard divider line.
     */
    public void showLine() {
        System.out.println(DIVIDER);
    }

    /**
     * Displays an error message.
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Displays every task currently in the list.
     */
    public void showTaskList(List<Task> tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int taskIndex = 0; taskIndex < tasks.size(); taskIndex++) {
            System.out.println((taskIndex + 1) + "." + tasks.get(taskIndex));
        }
    }

    /**
     * Confirms that a task was added and shows the updated task count.
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task: " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Confirms that a task was marked as done.
     */
    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done: " + task);
    }

    /**
     * Confirms that a task was marked as not done.
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet: " + task);
    }

    /**
     * Confirms that a task was deleted and shows the updated task count.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task: " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays a task created from a plain task description.
     */
    public void showPlainTask(Task task) {
        System.out.println(task);
    }

    /**
     * Displays the closing message.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Releases resources held by this UI.
     */
    public void close() {
        scanner.close();
    }
}
