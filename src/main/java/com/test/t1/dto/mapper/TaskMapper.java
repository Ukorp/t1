package com.test.t1.dto.mapper;

import com.test.t1.dto.TaskRequest;
import com.test.t1.dto.TaskResponse;
import com.test.t1.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {

    Task toTaskRequest(TaskRequest task);

    TaskResponse toTaskResponse(Task task);
}
