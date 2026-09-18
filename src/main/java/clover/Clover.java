package clover;

import java.io.IOException;

import clover.command.Command;
import clover.command.CommandResponseStyle;
import clover.exception.CloverException;
import clover.parser.Parser;
import clover.storage.Storage;
import clover.task.TaskList;
import clover.tutoree.TutoreeList;
import clover.ui.Ui;

/**
 * Coordinates Clover's user interface, task list, command parser, and storage.
 */
public class Clover {
    private final Storage storage;
    private final Ui ui;
    private TaskList tasks;
    private TutoreeList tutorees;
    private CommandResponseStyle responseStyle;

    /**
     * Creates Clover and loads its previously saved task list.
     */
    public Clover() {
        storage = new Storage();
        ui = new Ui();

        try {
            tasks = new TaskList(storage.load());
        } catch (IOException | SecurityException exception) {
            ui.showError("The saved quest journal could not be opened. Starting with an empty grove.");
            tasks = new TaskList();
        }
        try {
            tutorees = new TutoreeList(storage.loadTutorees());
        } catch (IOException | SecurityException exception) {
            ui.showError("The learning companion journal could not be opened. Starting with an empty grove.");
            tutorees = new TutoreeList();
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
                command.execute(tasks, tutorees, ui, storage);
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
        responseStyle = CommandResponseStyle.STANDARD;
        try {
            Command command = Parser.parse(input);
            responseStyle = command.getResponseStyle();
            command.execute(tasks, tutorees, ui, storage);
        } catch (CloverException exception) {
            responseStyle = CommandResponseStyle.ERROR;
            ui.showError(exception.getMessage());
        }
        return ui.buildResponse();
    }

    /**
     * Returns the visual style for the latest response.
     *
     * @return the visual style associated with the latest response
     */
    public CommandResponseStyle getResponseStyle() {
        return responseStyle;
    }

    /**
     * Starts the Clover application.
     */
    public static void main(String[] args) {
        new Clover().run();
    }
}
