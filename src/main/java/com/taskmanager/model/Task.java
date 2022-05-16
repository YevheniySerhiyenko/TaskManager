package com.taskmanager.model;

import java.time.LocalDate;
import java.util.Objects;

public class Task implements OrderEntity {
    private Long id;
    private String name;
    private Integer index;
    private String description;
    private LocalDate createdAt;
    private Long columnId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
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

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name)
                && Objects.equals(index, task.index)
                && Objects.equals(description, task.description)
                && Objects.equals(createdAt, task.createdAt)
                && Objects.equals(columnId, task.columnId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, index, description, createdAt, columnId);
    }
}
