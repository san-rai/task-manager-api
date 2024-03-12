package sandeshrai.taskmanager.api.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import sandeshrai.taskmanager.api.model.Task;
import sandeshrai.taskmanager.api.model.TaskStatus;

import java.util.List;

public interface TasksRepository extends ListCrudRepository<Task,Long> {

    @Query("""
            SELECT * FROM task
            where status = :status
            """)
    List<Task> filterByStatus(@Param("status") TaskStatus status);
}
