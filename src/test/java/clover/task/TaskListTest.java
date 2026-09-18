package clover.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Test
    void constructor_sourceListChanged_taskListIsUnchanged() {
        List<Task> sourceTasks = new ArrayList<>();
        sourceTasks.add(new ToDo("read book"));
        TaskList taskList = new TaskList(sourceTasks);

        sourceTasks.add(new ToDo("buy groceries"));

        assertEquals(1, taskList.size());
    }

    @Test
    void asList_taskAddedToReturnedList_unsupportedOperationExceptionThrown() {
        TaskList taskList = new TaskList();

        assertThrows(UnsupportedOperationException.class, () -> taskList.asList().add(new ToDo("read book")));
    }

    @Test
    void containsEquivalent_sameTypeAndDetails_trueReturnedRegardlessOfCompletionStatus() {
        TaskList taskList = new TaskList();
        ToDo existingTask = new ToDo("read book", "Alice");
        existingTask.markAsDone();
        taskList.add(existingTask);

        assertTrue(taskList.containsEquivalent(new ToDo("read book", "Alice")));
        assertFalse(taskList.containsEquivalent(new Deadline("read book", LocalDate.of(2026, 9, 1), "Alice")));
    }
}
