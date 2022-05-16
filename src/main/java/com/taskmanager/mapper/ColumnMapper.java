package com.taskmanager.mapper;

import com.taskmanager.dto.column.ColumnDto;
import com.taskmanager.model.Column;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class ColumnMapper {
    public static ColumnDto toDto(@NotNull Column column){
        ColumnDto columnDto = new ColumnDto();
        columnDto.setId(column.getId());
        columnDto.setName(column.getName());

        return columnDto;
    }

    public static List<ColumnDto> listToDto(@NotNull List<Column> list) {
        return list.stream()
                .map(ColumnMapper::toDto)
                .collect(Collectors.toList());
    }
}
