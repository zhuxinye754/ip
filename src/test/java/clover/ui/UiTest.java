package clover.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import clover.task.ToDo;

/**
 * Tests for UI messages captured for the JavaFX chat interface.
 */
class UiTest {

    @Test
    void showError_errorShown_responseContainsMessage() {
        Ui ui = new Ui();

        ui.showError("Invalid command");

        assertEquals("Invalid command", ui.getResponse());
    }

    @Test
    void clearResponse_messageShown_responseCleared() {
        Ui ui = new Ui();
        ui.showError("Invalid command");

        ui.clearResponse();

        assertEquals("", ui.getResponse());
    }

    @Test
    void showTaskAdded_taskAdded_responseContainsBothMessagesInOrder() {
        Ui ui = new Ui();

        ui.showTaskAdded(new ToDo("read book"), 1);

        assertEquals("Got it. I've added this task: [T] [ ] read book\n"
                + "Now you have 1 tasks in the list.", ui.getResponse());
    }
}
