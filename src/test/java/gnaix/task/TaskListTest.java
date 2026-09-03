package gnaix.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TaskListTest {

    @Test
    void add_emptyTaskList_taskAdded() {
        TaskList taskList = new TaskList();
        Task task = new Todo("buy milk");

        taskList.add(task);

        assertEquals(1, taskList.size());
        assertEquals(task, taskList.get(0));
    }

    @Test
    void add_existingTaskList_taskAddedAtEnd() {
        TaskList taskList = new TaskList();
        Task first = new Todo("buy milk");
        Task second = new Todo("buy eggs");

        taskList.add(first);
        taskList.add(second);

        assertEquals(2, taskList.size());
        assertEquals(first, taskList.get(0));
        assertEquals(second, taskList.get(1));
    }

    @Test
    void delete_middleIndex_taskRemovedAndOrderPreserved() {
        TaskList taskList = new TaskList();
        Task first = new Todo("buy milk");
        Task second = new Todo("buy eggs");
        Task third = new Todo("buy bread");

        taskList.add(first);
        taskList.add(second);
        taskList.add(third);

        Task deletedTask = taskList.delete(1);

        assertEquals(second, deletedTask);
        assertEquals(2, taskList.size());
        assertEquals(first, taskList.get(0));
        assertEquals(third, taskList.get(1));
    }

    @Test
    void mark_validIndex_taskMarkedComplete() {
        TaskList taskList = new TaskList();
        Task task = new Todo("buy milk");

        taskList.add(task);
        taskList.mark(0);

        assertTrue(taskList.get(0).isCompleted());
    }

    @Test
    void unmark_completedTask_taskMarkedIncomplete() {
        TaskList taskList = new TaskList();
        Task task = new Todo("buy milk");

        task.markAsComplete();
        taskList.add(task);

        taskList.unmark(0);

        assertFalse(taskList.get(0).isCompleted());
    }

    @Test
    void getLast_multipleTasks_lastTaskReturned() {
        TaskList taskList = new TaskList();
        Task first = new Todo("buy milk");
        Task second = new Todo("buy eggs");

        taskList.add(first);
        taskList.add(second);

        assertEquals(second, taskList.getLast());
    }
}
