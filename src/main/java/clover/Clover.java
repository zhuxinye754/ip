package clover;

import java.io.IOException;

import clover.command.Command;
import clover.exception.CloverException;
import clover.parser.Parser;
import clover.storage.Storage;
import clover.task.TaskList;
import clover.ui.Ui;

/**
 * Coordinates Clover's user interface, task list, command parser, and storage.
 */
public class Clover {
    private final Storage storage;
    private final Ui ui;
    private TaskList tasks;
    private String commandType;

    /**
     * Creates Clover and loads its previously saved task list.
     */
    public Clover() {
        storage = new Storage();
        ui = new Ui();

        try {
            tasks = new TaskList(storage.load());
        } catch (IOException | SecurityException exception) {
            ui.showError("I could not load your saved tasks. Starting with an empty list.");
            tasks = new TaskList();
        }
    }

    /**
     * Runs the command loop until the user enters {@code bye} or input ends.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit && ui.hasNextCommand()) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();
                Command command = Parser.parse(fullCommand);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (CloverException exception) {
                ui.showError(exception.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    /**
     * Generates a response for a user's chat message.
     *
     * @param input the message entered by the user
     * @return Clover's response to the message
     */
    public String getResponse(String input) {
        ui.clearResponse();
        commandType = null;
        try {
            Command command = Parser.parse(input);
            commandType = command.getClass().getSimpleName();
            command.execute(tasks, ui, storage);
        } catch (CloverException exception) {
            ui.showError(exception.getMessage());
        }
        return ui.getResponse();
    }

    /**
     * Returns the type of command that produced the latest response.
     *
     * @return the latest command type, or {@code null} when parsing failed
     */
    public String getCommandType() {
        return commandType;
    }

    /**
     * Starts the Clover application.
     */
    public static void main(String[] args) {
        new Clover().run();
    }
}
