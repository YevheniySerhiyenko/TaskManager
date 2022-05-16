package com.taskmanager.repository;

import com.taskmanager.model.Column;

import java.util.List;
import java.util.Optional;

public interface ColumnRepository {
    Long create(Column column);

    void update(Column column);

    void updateAll(List<Column> columns);

    void delete(Long columnId);

    Optional<Column> findById(Long id);

    Optional<Column> findByIndex(Integer index);

    List<Column> findAll();

    List<Column> findAllByRange(Integer startIndex, Integer endIndex);

    Integer getCount();

    boolean existsById(Long id);
}
