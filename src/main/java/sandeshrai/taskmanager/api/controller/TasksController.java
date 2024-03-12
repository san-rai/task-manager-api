package sandeshrai.taskmanager.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sandeshrai.taskmanager.api.model.Task;
import sandeshrai.taskmanager.api.model.TaskStatus;
import sandeshrai.taskmanager.api.repository.TasksRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TasksController {

    private final TasksRepository repository;

    TasksController(TasksRepository repository) {
        this.repository = repository;
    }

    @GetMapping("")
    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTask(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task with ID " + id + " not found");
        }
        Optional<Task> task = repository.findById(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void addTask(@RequestBody Task newTask) {
        repository.save(newTask);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTask(@PathVariable Long id, @RequestBody Task newTask) {
        if(!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with ID " + id + " not found");
        }

        Task updatedTask = new Task(id, newTask.name(), newTask.description(), newTask.status());
        repository.save(updatedTask);
    }

    @PutMapping("/{id}/{status}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTaskByStatus(@PathVariable Long id, @PathVariable TaskStatus status) {
        Optional<Task> optionalTask = repository.findById(id);
        if (optionalTask.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with ID " + id + " not found");
        }

        Task prevTask = optionalTask.get();

        Task newTask = new Task(
                id,
                prevTask.name(),
                prevTask.description(),
                status
        );

        repository.save(newTask);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        if(!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with ID " + id + " not found");
        }

        repository.deleteById(id);
    }
}
