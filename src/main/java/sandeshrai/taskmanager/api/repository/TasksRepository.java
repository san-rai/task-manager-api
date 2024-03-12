package sandeshrai.taskmanager.api.repository;

import org.springframework.data.repository.ListCrudRepository;
import sandeshrai.taskmanager.api.model.Task;

public interface TasksRepository extends ListCrudRepository<Task,Long> { }
