package com.taskmanager.service;

import com.taskmanager.exception.NotEmptyException;
import com.taskmanager.exception.NotFoundException;
import com.taskmanager.exception.WrongArgumentException;
import com.taskmanager.model.Column;
import com.taskmanager.model.OrderEntity;
import com.taskmanager.repository.ColumnRepository;
import com.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColumnServiceImpl implements ColumnService {
    private final ColumnRepository columnRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public ColumnServiceImpl(ColumnRepository columnRepository, TaskRepository taskRepository) {
        this.columnRepository = columnRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public Column create(String name) {
        int count = columnRepository.getCount();
        int actualIndex = count + 1;

        Column column = new Column();
        column.setName(name);
        column.setIndex(actualIndex);

        Long columnId = columnRepository.create(column);

        return columnRepository.findById(columnId)
                .orElseThrow(() -> new NotFoundException(
                        "Column with id: " + columnId + " not exist"));
    }

    @Override
    public Column update(Long columnId, String name) {
        Column column = this.findById(columnId);

        column.setName(name);
        columnRepository.update(column);

        return column;
    }

    @Override
    public void delete(Long columnId) {
        Column column = this.findById(columnId);

        if (taskRepository.existByColumnId(columnId)) {
            throw new NotEmptyException("Column: " + column.getName() + " not empty");
        }

        columnRepository.delete(column.getId());
    }


    @Override
    public Column findById(Long columnId) {
        return columnRepository.findById(columnId)
                .orElseThrow(
                        () -> new NotFoundException(
                                "Column with id: " + columnId + " not found"));
    }

    @Override
    public List<Column> findAll() {
        return columnRepository.findAll();
    }

    @Override
    public void changeIndex(Long id, Integer index) {
        Column column = this.findById(id);

        if (index > columnRepository.getCount()) {
            throw new WrongArgumentException("Index: " + index + " incorrect");
        }

        Integer columnIndex = column.getIndex();
        boolean indexHigher = columnIndex < index;

        List<Column> columns = indexHigher
                ? columnRepository.findAllByRange(columnIndex, index)
                : columnRepository.findAllByRange(index, columnIndex);

        List<? extends OrderEntity> list = ChangeIndexService.changeIndex(columns, indexHigher);

        columnRepository.updateAll((List<Column>) list);//need to fix
    }
}
