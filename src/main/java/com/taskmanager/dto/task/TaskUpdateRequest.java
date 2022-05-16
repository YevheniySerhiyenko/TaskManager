package com.taskmanager.dto.task;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class TaskUpdateRequest {
    @Size(min = 2, max = 30)
    @NotNull
    private String name;

    @Size(min = 10, max = 200)
    @NotNull
    private String description;
    @NotNull

    @NotNull
    private LocalDate createdAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
