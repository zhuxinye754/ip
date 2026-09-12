package clover.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

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

        assertEquals("Invalid command", ui.buildResponse());
    }

    @Test
    void clearResponse_messageShown_responseCleared() {
        Ui ui = new Ui();
        ui.showError("Invalid command");

        ui.clearResponse();

        assertEquals("", ui.buildResponse());
    }

    @Test
    void showTaskAdded_taskAdded_responseContainsBothMessagesInOrder() {
        Ui ui = new Ui();

        ui.showTaskAdded(new ToDo("read book"), 1);

        assertEquals("Got it. I've added this task: [T] [ ] read book\n"
                + "Now you have 1 tasks in the list.", ui.buildResponse());
    }

    @Test
    void showTaskList_tasksSupplied_responseListsTasksInNumberedOrder() {
        Ui ui = new Ui();

        ui.showTaskList(List.of(new ToDo("read book"), new ToDo("buy groceries")));

        assertEquals("Here are the tasks in your list:\n"
                + "1.[T] [ ] read book\n"
                + "2.[T] [ ] buy groceries", ui.buildResponse());
    }

    @Test
    void showFindResults_tasksSupplied_responseListsMatchingTasksInNumberedOrder() {
        Ui ui = new Ui();

        ui.showFindResults(List.of(new ToDo("read book"), new ToDo("buy bookcase")));

        assertEquals("Here are the matching tasks in your list:\n"
                + "1.[T] [ ] read book\n"
                + "2.[T] [ ] buy bookcase", ui.buildResponse());
    }
}
