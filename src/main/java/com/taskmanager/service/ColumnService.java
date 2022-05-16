package com.taskmanager.service;

import com.taskmanager.model.Column;

import java.util.List;

public interface ColumnService {
    Column create(String name);

    Column update(Long columnId, String name);

    void delete(Long columnId);

    Column findById(Long columnId);

    List<Column> findAll();

    void changeIndex(Long id, Integer index);
}
