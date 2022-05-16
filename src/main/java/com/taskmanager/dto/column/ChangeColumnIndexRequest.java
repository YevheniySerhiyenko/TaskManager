package com.taskmanager.dto.column;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ChangeColumnIndexRequest {
    @NotNull
    @Min(1)
    private Integer index;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
