package sandeshrai.taskmanager.api.repository;

import org.springframework.data.repository.ListCrudRepository;
import sandeshrai.taskmanager.api.model.Task;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public interface TasksRepository extends ListCrudRepository<Task,Long> { }
