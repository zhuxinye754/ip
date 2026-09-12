package clover.task;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

/**
 * Tests assumptions enforced by {@link TaskList}.
 */
class TaskListTest {

    @Test
    void constructor_nullBackingCollection_assertionErrorThrown() {
        assertThrows(AssertionError.class, () -> new TaskList(null));
    }

    @Test
    void add_nullTask_assertionErrorThrown() {
        TaskList taskList = new TaskList(new ArrayList<>());

        assertThrows(AssertionError.class, () -> taskList.add(null));
    }

    @Test
    void getLast_emptyTaskList_assertionErrorThrown() {
        TaskList taskList = new TaskList();

        assertThrows(AssertionError.class, taskList::getLast);
    }
}
