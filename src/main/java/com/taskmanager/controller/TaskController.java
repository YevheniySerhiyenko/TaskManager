package com.taskmanager.controller;

import com.taskmanager.dto.task.ChangeTaskIndexRequest;
import com.taskmanager.dto.task.TaskCreateRequest;
import com.taskmanager.dto.task.TaskDto;
import com.taskmanager.dto.task.TaskUpdateRequest;
import com.taskmanager.mapper.TaskMapper;
import com.taskmanager.model.Column;
import com.taskmanager.model.Task;
import com.taskmanager.service.ColumnService;
import com.taskmanager.service.TaskService;
import com.taskmanager.service.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class TaskController {
    private final TaskService taskService;
    private final ColumnService columnService;

    @Autowired
    public TaskController(TaskServiceImpl taskService, ColumnService columnService) {
        this.taskService = taskService;
        this.columnService = columnService;
    }

    @PostMapping("/columns/{columnId}/tasks")
    public TaskDto create(@PathVariable Long columnId,
                          @Valid @RequestBody TaskCreateRequest request
    ) {
        Column column = columnService.findById(columnId);
        Task user = taskService.create(
                request.getName(),
                request.getDescription(),
                request.getCreatedAt(),
                column
        );

        return TaskMapper.toDto(user);
    }

    @PutMapping("/columns/{id}/tasks")
    public TaskDto update(@PathVariable Long id,
                          @Valid @RequestBody TaskUpdateRequest request
    ) {
        Task task = taskService.update(
                id,
                request.getName(),
                request.getDescription(),
                request.getCreatedAt()
        );

        return TaskMapper.toDto(task);
    }

    @GetMapping("/columns/{columnId}/tasks/{id}")
    public TaskDto findTaskInColumn(
            @PathVariable Long columnId,
            @PathVariable Long id
    ) {
        Column column = columnService.findById(columnId);

        return TaskMapper.toDto(taskService.findByColumnAndId(column, id));
    }

    @GetMapping("/columns/{id}/tasks")
    public List<TaskDto> findAllByColumn(@PathVariable Long id) {
        Column column = columnService.findById(id);

        return TaskMapper.listToDto(taskService.findAllByColumn(column));
    }

    @DeleteMapping("/tasks/{id}")
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }

    @PatchMapping("/columns/{columnId}/tasks/{taskId}")
    public void moveToAnotherColumn(
            @PathVariable Long columnId,
            @PathVariable Long taskId
    ) {
        Column column = columnService.findById(columnId);
        taskService.moveToAnotherColumn(taskId, column);
    }

    @PatchMapping("/columns/{id}/tasks")
    public void changeIndex(
            @PathVariable Long id,
            @Valid @RequestBody ChangeTaskIndexRequest request
    ) {
        Column column = columnService.findById(id);
        taskService.changeIndex(column, request.getId(), request.getIndex());
    }
}
