package com.taskmanager.service;

import com.taskmanager.model.Column;
import com.taskmanager.model.Task;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    Task create(String name, String description, LocalDate createdAt, Column column);

    Task update(Long id, String name, String description, LocalDate createdAt);

    Task findByColumnAndId(Column column, Long id);

    List<Task> findAllByColumn(Column column);

    void delete(Long id);

    void moveToAnotherColumn(Long id, Column column);

    void changeIndex(Column column, Long id, Integer index);
}
