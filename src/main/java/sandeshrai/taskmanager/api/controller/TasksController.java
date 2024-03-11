package sandeshrai.taskmanager.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sandeshrai.taskmanager.api.model.Task;
import sandeshrai.taskmanager.api.model.TaskContent;
import sandeshrai.taskmanager.api.repository.TasksCollectionRepository;

import java.util.ArrayList;

@RestController
@RequestMapping("/tasks")
public class TasksController {

    private final TasksCollectionRepository repository;

    TasksController(TasksCollectionRepository repository) {
        this.repository = repository;
    }

    @GetMapping("")
    public ArrayList<Task> getAllTasks() {
        return repository.getAllTasks();
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void addTask(@RequestBody TaskContent taskContent) {
        repository.addTask(taskContent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTask(@PathVariable String id, @RequestBody TaskContent taskContent) {
        if (!repository.checkTaskIdExists(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task with ID " + id + " not found");
        }

        repository.updateTask(id, taskContent);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> updateTask(@PathVariable String id) {
        if (!repository.checkTaskIdExists(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task with ID " + id + " not found");
        }

        repository.deleteTask(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
