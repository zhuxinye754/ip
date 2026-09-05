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
    private final StringBuilder response;

    /**
     * Creates a UI that reads commands from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
        response = new StringBuilder();
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
        showMessages(
                DIVIDER,
                banner,
                "Hello! I'm Clover.",
                "What can I do for you?\n",
                DIVIDER);
    }

    /**
     * Displays the standard divider line.
     */
    public void showLine() {
        showMessage(DIVIDER);
    }

    /**
     * Displays an error message.
     */
    public void showError(String message) {
        showMessage(message);
    }

    /**
     * Displays every task currently in the list.
     */
    public void showTaskList(List<Task> tasks) {
        showMessage("Here are the tasks in your list:");
        for (int taskIndex = 0; taskIndex < tasks.size(); taskIndex++) {
            showMessage((taskIndex + 1) + "." + tasks.get(taskIndex));
        }
    }

    /**
     * Confirms that a task was added and shows the updated task count.
     */
    public void showTaskAdded(Task task, int taskCount) {
        showMessages(
                "Got it. I've added this task: " + task,
                "Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Confirms that a task was marked as done.
     */
    public void showTaskMarked(Task task) {
        showMessage("Nice! I've marked this task as done: " + task);
    }

    /**
     * Confirms that a task was marked as not done.
     */
    public void showTaskUnmarked(Task task) {
        showMessage("OK, I've marked this task as not done yet: " + task);
    }

    /**
     * Confirms that a task was deleted and shows the updated task count.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        showMessages(
                "Noted. I've removed this task: " + task,
                "Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays all the tasks whose description contains a given keyword.
     */
    public void showFindResults(List<Task> result) {
        showMessage("Here are the matching tasks in your list:");
        for (int taskIndex = 0; taskIndex < result.size(); taskIndex++) {
            showMessage((taskIndex + 1) + "." + result.get(taskIndex));
        }
    }

    /**
     * Displays a task created from a plain task description.
     */
    public void showPlainTask(Task task) {
        showMessage(task.toString());
    }

    /**
     * Displays the closing message.
     */
    public void showGoodbye() {
        showMessage("Bye. Hope to see you again soon!");
    }

    /**
     * Clears messages collected for the next GUI response.
     */
    public void clearResponse() {
        response.setLength(0);
    }

    /**
     * Returns the messages produced since the response was last cleared.
     */
    public String getResponse() {
        return response.toString().stripTrailing();
    }

    /**
     * Prints a message and keeps a copy so it can be displayed by the GUI.
     */
    private void showMessage(String message) {
        System.out.println(message);
        response.append(message).append(System.lineSeparator());
    }

    /**
     * Displays a sequence of related messages in their supplied order.
     *
     * @param messages the messages to display
     */
    private void showMessages(String... messages) {
        for (String message : messages) {
            showMessage(message);
        }
    }

    /**
     * Releases resources held by this UI.
     */
    public void close() {
        scanner.close();
    }
}
