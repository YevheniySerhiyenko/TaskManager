package com.taskmanager.service;

import com.taskmanager.exception.NotFoundException;
import com.taskmanager.exception.WrongArgumentException;
import com.taskmanager.model.Column;
import com.taskmanager.model.OrderEntity;
import com.taskmanager.model.Task;
import com.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task create(String name, String description, LocalDate createdAt, Column column) {
        int count = taskRepository.getCountInColumn(column.getId());
        int actualIndex = count + 1;

        Task task = new Task();

        task.setName(name);
        task.setIndex(actualIndex);
        task.setDescription(description);
        task.setCreatedAt(createdAt);
        task.setColumnId(column.getId());

        Long id = taskRepository.create(task);

        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Task by id: " + id + " not found"));
    }

    @Override
    public Task update(
            Long id,
            String name,
            String description,
            LocalDate createdAt
    ) {
        Task task = this.findById(id);

        task.setName(name);
        task.setDescription(description);
        task.setCreatedAt(createdAt);

        taskRepository.update(task);

        return task;
    }

    @Override
    public Task findByColumnAndId(Column column, Long id) {
        return taskRepository.findByColumnIdAndTaskId(column.getId(), id)
                .orElseThrow(() -> new NotFoundException(
                        "Task with id: " + id + " not found"));
    }


    private Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Task with id: " + id + " not found"));
    }


    @Override
    public List<Task> findAllByColumn(Column column) {
        return taskRepository.findAllByColumn(column.getId());
    }

    @Override
    public void delete(Long id) {
        if (!taskRepository.existById(id)) {
            throw new NotFoundException("Task with id: " + id + " not found");
        }

        taskRepository.delete(id);
    }

    @Override
    public void moveToAnotherColumn(Long id, Column column) {
        Task task = this.findById(id);

        task.setColumnId(column.getId());
        taskRepository.update(task);
    }

    @Override
    public void changeIndex(Column column, Long id, Integer index) {
        if (!taskRepository.existById(id)) {
            throw new NotFoundException(
                    "Task with id: " + id + " not exist");
        }
        if (index > taskRepository.getCountInColumn(column.getId())) {
            throw new WrongArgumentException("Index: " + index + " incorrect");
        }

        Task task = this.findByColumnAndId(column, id);

        Long columnId = column.getId();
        Integer taskIndex = task.getIndex();

        boolean indexHigher = taskIndex < index;
        List<Task> taskList =
                indexHigher
                        ? taskRepository.findAllByRange(columnId, taskIndex, index)
                        : taskRepository.findAllByRange(columnId, index, taskIndex);

        List<? extends OrderEntity> list = ChangeIndexService.changeIndex(taskList, indexHigher);
        taskRepository.updateAll((List<Task>) list);//need to fix
    }
}
