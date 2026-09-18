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

        assertEquals("The forest path is unclear. Invalid command", ui.buildResponse());
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

        assertEquals("A new study quest has taken root: [T] [ ] read book" + System.lineSeparator()
                + "The grove now holds 1 quest.", ui.buildResponse());
    }

    @Test
    void showTaskList_tasksSupplied_responseListsTasksInNumberedOrder() {
        Ui ui = new Ui();

        ui.showTaskList(List.of(new ToDo("read book"), new ToDo("buy groceries")));

        assertEquals("The grove has gathered your study quests:" + System.lineSeparator()
                + "1.[T] [ ] read book" + System.lineSeparator()
                + "2.[T] [ ] buy groceries", ui.buildResponse());
    }

    @Test
    void showTaskList_emptyTaskList_clearEmptyMessageShown() {
        Ui ui = new Ui();

        ui.showTaskList(List.of());

        assertEquals("There are no study quests in the grove yet.", ui.buildResponse());
    }

    @Test
    void showHelp_commandReferenceShown() {
        Ui ui = new Ui();

        ui.showHelp();

        assertEquals(String.join(System.lineSeparator(),
                "How to use Clover",
                "Type lowercase words exactly. Replace <UPPERCASE> placeholders with your details.",
                "[Square brackets show optional parts; do not type the brackets.]",
                "",
                "TASKS",
                "• todo <DESCRIPTION> [/for <NAME>]",
                "  Add a task; /for links it to a tutoree.",
                "• deadline <DESCRIPTION> /by <DATE> [/for <NAME>]",
                "  Add a deadline. DATE uses yyyy-MM-dd.",
                "• event <DESCRIPTION> /from <DATE> /to <DATE> [/for <NAME>]",
                "  Add an event. The end date must be after the start date.",
                "• list",
                "  Show all tasks.",
                "• find <KEYWORD>",
                "  Search task descriptions.",
                "• mark <NUMBER>",
                "  Complete a task.",
                "• unmark <NUMBER>",
                "  Reopen a completed task.",
                "• delete <NUMBER>",
                "  Remove a task.",
                "",
                "TUTOREE DIRECTORY",
                "• add-tutoree <NAME> /address <ADDRESS> /fee <AMOUNT>[/RATE]",
                "  Add a tutoree. RATE may be /hour, /session, /lesson, or /month.",
                "• list-tutorees",
                "  Show all tutorees.",
                "• find-tutoree <KEYWORD>",
                "  Search tutoree names.",
                "",
                "OTHER",
                "• help",
                "  Show this command guide.",
                "• bye",
                "  Close Clover."), ui.buildResponse());
    }

    @Test
    void showFindResults_tasksSupplied_responseListsMatchingTasksInNumberedOrder() {
        Ui ui = new Ui();

        ui.showFindResults(List.of(new ToDo("read book"), new ToDo("buy bookcase")));

        assertEquals("The grove found these matching quests:" + System.lineSeparator()
                + "1.[T] [ ] read book" + System.lineSeparator()
                + "2.[T] [ ] buy bookcase", ui.buildResponse());
    }
}
