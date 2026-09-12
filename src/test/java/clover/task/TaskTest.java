package clover.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Tests the completion state of a task. */
class TaskTest {

    @Test
    void markAsDone_newTodo_taskMarkedAndDisplayedAsDone() {
        Task task = new ToDo("read book");

        task.markAsDone();

        assertTrue(task.isDone());
        assertEquals("[T] [X] read book", task.toString());
    }

    @Test
    void markAsUndone_completedTask_taskUnmarkedAndDisplayedAsIncomplete() {
        Task task = new ToDo("read book");
        task.markAsDone();

        task.markAsUndone();

        assertFalse(task.isDone());
        assertEquals("[T] [ ] read book", task.toString());
    }

    @Test
    void constructor_nullDescription_assertionErrorThrown() {
        assertThrows(AssertionError.class, () -> new ToDo(null));
    }
}
