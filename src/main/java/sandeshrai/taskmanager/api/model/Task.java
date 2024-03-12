package sandeshrai.taskmanager.api.model;

import org.springframework.data.annotation.Id;

public record Task(
        @Id
        Long id,
        String name,
        String description,
        TaskStatus status
) { }
