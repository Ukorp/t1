package com.test.t1.dto.mapper;

import com.test.t1.dto.TaskRequest;
import com.test.t1.dto.TaskResponse;
import com.test.t1.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {

    @Mapping(target = "status", defaultValue = "RECEIVED")
    Task toTask(TaskRequest task);

    TaskResponse toTaskResponse(Task task);
}
