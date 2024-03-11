package sandeshrai.taskmanager.api.repository;

import org.springframework.stereotype.Repository;
import sandeshrai.taskmanager.api.model.Task;
import sandeshrai.taskmanager.api.model.TaskContent;

import java.util.ArrayList;
import java.util.UUID;

@Repository
public class TasksCollectionRepository {
    private ArrayList<Task> tasksList = new ArrayList<>();

    public ArrayList<Task> getAllTasks() {
        return tasksList;
    }

    public void addTask(TaskContent taskContent) {
        String id = UUID.randomUUID().toString();
        Task newTask = new Task(id, taskContent);

        tasksList.add(newTask);
    }
}
