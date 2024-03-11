package sandeshrai.taskmanager.api.repository;

import org.springframework.stereotype.Repository;
import sandeshrai.taskmanager.api.model.Task;
import sandeshrai.taskmanager.api.model.TaskContent;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class TasksCollectionRepository {
    private ArrayList<Task> tasksList = new ArrayList<>();

    public ArrayList<Task> getAllTasks() {
        return tasksList;
    }

    public Task getTask(String id) {

        return tasksList.stream()
                .filter(task -> Objects.equals(task.id(), id))
                .findFirst()
                .orElseThrow();
    }

    public void addTask(TaskContent taskContent) {
        String id = UUID.randomUUID().toString();
        Task newTask = new Task(id, taskContent);

        tasksList.add(newTask);
    }

    public boolean checkTaskIdExists(String id) {
        return (tasksList.stream().anyMatch(task -> Objects.equals(task.id(), id)));
    }

    public void updateTask(String id, TaskContent taskContent) {
        this.deleteTask(id);

        Task newTask = new Task(id, taskContent);
        tasksList.add(newTask);
    }

    public void deleteTask(String id) {
        tasksList = (ArrayList<Task>) tasksList.stream()
                .filter(task -> !Objects.equals(task.id(), id))
                .collect(Collectors.toList());
    }
}
