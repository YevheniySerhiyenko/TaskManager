package com.taskmanager.controller;

import com.taskmanager.dto.column.ChangeColumnIndexRequest;
import com.taskmanager.dto.column.ColumnCreateRequest;
import com.taskmanager.dto.column.ColumnDto;
import com.taskmanager.dto.column.ColumnUpdateRequest;
import com.taskmanager.mapper.ColumnMapper;
import com.taskmanager.service.ColumnService;
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
public class ColumnController {
    private final ColumnService columnService;

    @Autowired
    public ColumnController(ColumnService columnService) {
        this.columnService = columnService;
    }

    @PostMapping("/columns")
    public ColumnDto create(@Valid @RequestBody ColumnCreateRequest request) {
        return ColumnMapper.toDto(
                columnService.create(request.getName()));
    }

    @PutMapping("/columns/{id}")
    public ColumnDto update(
            @PathVariable Long id,
            @Valid @RequestBody ColumnUpdateRequest request) {

        return ColumnMapper.toDto(
                columnService.update(id, request.getName()));
    }

    @DeleteMapping("/columns/{id}")
    void deleteColumn(@PathVariable Long id) {
        columnService.delete(id);
    }

    @GetMapping("/columns")
    public List<ColumnDto> findAll() {
        return ColumnMapper.listToDto(columnService.findAll());

    }

    @PatchMapping("/columns/{id}")
    public void changeIndex(
            @PathVariable Long id,
            @Valid @RequestBody ChangeColumnIndexRequest request
    ) {
        columnService.changeIndex(id, request.getIndex());
    }
}
