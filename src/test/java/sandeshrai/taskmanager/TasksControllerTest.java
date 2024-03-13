package sandeshrai.taskmanager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import sandeshrai.taskmanager.api.controller.TasksController;
import sandeshrai.taskmanager.api.model.Task;
import sandeshrai.taskmanager.api.model.TaskStatus;
import sandeshrai.taskmanager.api.repository.TasksRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TasksControllerTest {

    @Mock
    private TasksRepository repository;

    @InjectMocks
    private TasksController controller;

    @Test
    public void testGetAllTasks() {
        // Mocking repository response
        Task task1 = new Task(1L, "Task 1", "Description 1", TaskStatus.TODO);
        Task task2 = new Task(2L, "Task 2", "Description 2", TaskStatus.IN_PROGRESS);

        when(repository.findAll()).thenReturn(Arrays.asList(task1, task2));

        // Calling controller method
        List<Task> tasks = controller.getAllTasks();

        // Asserting properties of the first task
        assertEquals(1L, tasks.getFirst().id());
        assertEquals("Task 1", tasks.getFirst().name());
        assertEquals("Description 1", tasks.getFirst().description());
        assertEquals(TaskStatus.TODO, tasks.getFirst().status());

        // Asserting properties of the second task
        assertEquals(2L, tasks.get(1).id());
        assertEquals("Task 2", tasks.get(1).name());
        assertEquals("Description 2", tasks.get(1).description());
        assertEquals(TaskStatus.IN_PROGRESS, tasks.get(1).status());
    }

    @Test
    public void getTaskById_TaskExists() {
        Task task1 = new Task(1L, "Task 1", "Description 1", TaskStatus.TODO);

        when(repository.existsById(1L)).thenReturn(true);
        when(repository.findById(1L)).thenReturn(Optional.of(task1));

        // Getting Task ID  that exists
        ResponseEntity<Object> responseTask1 = controller.getTaskById(1L);

        assertEquals(HttpStatus.OK, responseTask1.getStatusCode());
        assertEquals(Optional.of(task1), responseTask1.getBody());
    }

    @Test
    public void getTaskById_TaskDoesNotExist() {
        when(repository.existsById(2L)).thenReturn(false);

        // Getting Task ID  that doesn't exist
        ResponseEntity<Object> responseTask2 = controller.getTaskById(2L);

        assertEquals(HttpStatus.NOT_FOUND, responseTask2.getStatusCode());
        assertEquals("Task with ID 2 not found", responseTask2.getBody());
    }


    @Test
    public void testAddTask() {
        Task newTask = new Task(1L, "Task 1", "Description 1", TaskStatus.TODO);

        controller.addTask(newTask);

        // Verifying that the repository's save() method was called with the correct task object
        verify(repository, times(1)).save(newTask);
    }

    @Test
    public void testUpdateTask_TaskExists() {
        long validId = 1L;

        Task updatedTask = new Task(validId, "Task 1 - Updated", "Description 1 - Updated", TaskStatus.IN_PROGRESS);

        when(repository.existsById(validId)).thenReturn(true);

        // Updating task with valid id
        controller.updateTask(validId, updatedTask);

        verify(repository, times(1)).save(updatedTask);
    }

    @Test
    public void testUpdateTask_TaskDoesNotExist() {
        long invalidId = 2L;

        Task updatedTask = new Task(invalidId, "Task 1 - Updated", "Description 1 - Updated", TaskStatus.IN_PROGRESS);

        when(repository.existsById(invalidId)).thenReturn(false);

        // Updating task with invalid id
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                ()->{
                    controller.updateTask(invalidId, updatedTask);
                });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Task with ID 2 not found", exception.getReason());
    }

    @Test
    public void testUpdateTaskByStatus_TaskExists() {
        long validId = 1L;

        Task originalTask = new Task(validId, "Task 1", "Description 1", TaskStatus.TODO);
        Optional<Task> optionalOriginalTask = Optional.of(originalTask);
        Task updatedTask = new Task(validId, "Task 1", "Description 1", TaskStatus.DONE);

        when(repository.findById(validId)).thenReturn(optionalOriginalTask);

        // Updating task status to DONE
        controller.updateTaskByStatus(validId, TaskStatus.DONE);

        verify(repository, times(1)).save(updatedTask);
    }

    @Test
    public void testUpdateTaskByStatus_TaskDoesNotExist() {
        long invalidId = 2L;

        Task updatedTask = new Task(invalidId, "Task 1", "Description 1", TaskStatus.TODO);

        when(repository.existsById(invalidId)).thenReturn(false);

        // Updating task with invalid id
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                ()->{
                    controller.updateTask(invalidId, updatedTask);
                });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Task with ID 2 not found", exception.getReason());
    }

    @Test
    public void testDeleteTask_TaskExists() {
        long validId = 1L;

        when(repository.existsById(validId)).thenReturn(true);

        // Deleting task with valid id
        controller.deleteTask(validId);

        verify(repository, times(1)).deleteById(validId);
    }

    @Test
    public void testDeleteTask_TaskDoesNotExist() {
        long invalidId = 2L;

        when(repository.existsById(invalidId)).thenReturn(false);

        // Deleting task with invalid id
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                ()->{
                    controller.deleteTask(invalidId);
                });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Task with ID 2 not found", exception.getReason());
    }

    @Test
    public void filterByStatus_WithResults() {
        TaskStatus statusToFilter = TaskStatus.TODO;

        Task task1 = new Task(1L, "Task 1", "Description 1", TaskStatus.TODO);
        Task task2 = new Task(2L, "Task 2", "Description 2", TaskStatus.TODO);

        when(repository
                .filterByStatus(statusToFilter))
                .thenReturn(Arrays.asList(task1, task2));

        List<Task> responseList = controller.filterByStatus(statusToFilter);

        verify(repository, times(1)).filterByStatus(statusToFilter);

        assertEquals(1L, responseList.getFirst().id());
        assertEquals("Task 1", responseList.getFirst().name());
        assertEquals("Description 1", responseList.getFirst().description());
        assertEquals(TaskStatus.TODO, responseList.getFirst().status());

        assertEquals(2L, responseList.get(1).id());
        assertEquals("Task 2", responseList.get(1).name());
        assertEquals("Description 2", responseList.get(1).description());
        assertEquals(TaskStatus.TODO, responseList.get(1).status());
    }

    @Test
    public void filterByStatus_WithNoResults() {
        TaskStatus statusToFilter = TaskStatus.DONE;
        List<Task> emptyList = List.of();

        when(repository.filterByStatus(statusToFilter))
                .thenReturn(emptyList);

        List<Task> responseList = controller.filterByStatus(statusToFilter);

        verify(repository, times(1)).filterByStatus(statusToFilter);
        assertEquals(emptyList, responseList);
    }
}
