package clover;

import clover.command.Command;
import clover.exception.CloverException;
import clover.parser.Parser;
import clover.storage.Storage;
import clover.task.TaskList;
import clover.ui.Ui;

import java.io.IOException;

/** Coordinates Clover's user interface, task list, command parser, and storage. */
public class Clover {
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
    /** Runs the command loop until the user enters {@code bye} or input ends. */
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
            } catch (CloverException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    /** Starts the Clover application. */
    public static void main(String[] args) {
        new Clover().run();
    }
}
