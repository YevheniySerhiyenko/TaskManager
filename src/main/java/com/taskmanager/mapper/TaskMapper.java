package com.taskmanager.mapper;

import com.taskmanager.dto.task.TaskDto;
import com.taskmanager.model.Task;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class TaskMapper {
    public static TaskDto toDto(@NotNull Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setName(task.getName());
        taskDto.setDescription(task.getDescription());
        taskDto.setCreatedAt(task.getCreatedAt());

        return taskDto;
    }

    public static List<TaskDto> listToDto(@NotNull List<Task> list) {
        return list.stream()
                .map(TaskMapper::toDto)
                .collect(Collectors.toList());
    }
}
