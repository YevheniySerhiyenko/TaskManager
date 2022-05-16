package com.taskmanager.dto.column;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ColumnUpdateRequest {
    @Size(min = 2, max = 30)
    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
