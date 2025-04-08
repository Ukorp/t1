package com.test.t1.dto.mapper;

import com.test.t1.dto.TaskRequest;
import com.test.t1.dto.TaskResponse;
import com.test.t1.model.Status;
import com.test.t1.model.Task;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-08T21:11:14+0300",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.13.jar, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public Task toTask(TaskRequest task) {
        if ( task == null ) {
            return null;
        }

        Task task1 = new Task();

        if ( task.status() != null ) {
            task1.setStatus( task.status() );
        }
        else {
            task1.setStatus( Status.RECEIVED );
        }
        task1.setUserId( (long) task.userId() );
        task1.setTitle( task.title() );
        task1.setDescription( task.description() );

        return task1;
    }

    @Override
    public TaskResponse toTaskResponse(Task task) {
        if ( task == null ) {
            return null;
        }

        Long id = null;
        String title = null;
        String description = null;
        int userId = 0;
        Status status = null;

        id = task.getId();
        title = task.getTitle();
        description = task.getDescription();
        if ( task.getUserId() != null ) {
            userId = task.getUserId().intValue();
        }
        status = task.getStatus();

        TaskResponse taskResponse = new TaskResponse( id, title, description, userId, status );

        return taskResponse;
    }
}
