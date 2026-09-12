package clover.command;

import clover.exception.CloverException;
import clover.storage.Storage;
import clover.task.TaskList;
import clover.tutoree.Tutoree;
import clover.tutoree.TutoreeList;
import clover.ui.Ui;

/**
 * Adds a tutoree with their name, tutoring address, and fee arrangement.
 */
public class AddTutoreeCommand extends Command {
    private static final String ADDRESS_MARKER = " /address";
    private static final String FEE_MARKER = " /fee";

    private final String arguments;

    /** Creates a command from the text after the {@code add-tutoree} keyword. */
    public AddTutoreeCommand(String arguments) {
        this.arguments = arguments;
    }

    /** Validates, adds, and saves a tutoree. */
    @Override
    public void execute(TaskList tasks, TutoreeList tutorees, Ui ui, Storage storage) throws CloverException {
        int addressIndex = arguments.indexOf(ADDRESS_MARKER);
        int feeIndex = arguments.indexOf(FEE_MARKER);
        if (addressIndex <= 0 || feeIndex <= addressIndex + ADDRESS_MARKER.length()) {
            throw invalidFormat();
        }

        String name = arguments.substring(0, addressIndex).trim();
        String address = arguments.substring(addressIndex + ADDRESS_MARKER.length(), feeIndex).trim();
        String fee = arguments.substring(feeIndex + FEE_MARKER.length()).trim();
        if (name.isEmpty() || address.isEmpty() || fee.isEmpty()) {
            throw invalidFormat();
        }
        if (tutorees.findExactName(name).isPresent()) {
            throw new CloverException("A tutoree named \"" + name + "\" already exists.");
        }

        Tutoree tutoree = new Tutoree(name, address, fee);
        tutorees.add(tutoree);
        saveTutorees(tutorees, ui, storage);
        ui.showTutoreeAdded(tutoree, tutorees.size());
    }

    /** Creates the shared guidance message for malformed add-tutoree commands. */
    private CloverException invalidFormat() {
        return new CloverException("Please use the format: add-tutoree NAME /address ADDRESS /fee FEE");
    }
}
