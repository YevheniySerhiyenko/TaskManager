package com.taskmanager.dto.task;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ChangeTaskIndexRequest {
    @NotNull
    private Long id;
    @NotNull
    @Min(1)
    private Integer index;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
