package sandeshrai.taskmanager.api.controller;

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
    public void addTask(@RequestBody TaskContent taskContent) {
        repository.addTask(taskContent);
    }
}
