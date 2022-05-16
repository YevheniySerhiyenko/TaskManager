package com.taskmanager.repository;

import com.taskmanager.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    Long create(Task task);

    void update(Task task);

    void delete(Long id);

    void updateAll(List<Task> tasks);

    Optional<Task> findById(Long id);

    List<Task> findAllByColumn(Long columnId);

    List<Task> findAllByRange(Long columnId, Integer startIndex, Integer endIndex);

    Optional<Task> findByColumnIdAndTaskId(Long columnId, Long id);

    boolean existById(Long id);

    int getCountInColumn(Long id);

    boolean existByColumnId(Long columnId);
}

