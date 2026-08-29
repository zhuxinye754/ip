/** Ends the Clover application after displaying a farewell message. */
public class ExitCommand extends Command {
    /** Displays the farewell message and closes console input. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
        ui.close();
    }

    /** Indicates that Clover should leave its command loop. */
    @Override
    public boolean isExit() {
        return true;
    }
}
