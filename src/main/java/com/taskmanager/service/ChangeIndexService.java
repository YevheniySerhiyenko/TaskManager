package com.taskmanager.service;

import com.taskmanager.model.OrderEntity;

import java.util.ArrayList;
import java.util.List;

public class ChangeIndexService {//I think need to fix
    public static List<? extends OrderEntity> changeIndex(List<? extends OrderEntity> list, boolean indexHigher) {
        List<OrderEntity> tasks = new ArrayList<>(list);

        if (indexHigher) {

            for (int i = tasks.size() - 1; i > 0; i--) {
                int temp = tasks.get(i).getIndex();
                tasks.get(i).setIndex(tasks.get(i - 1).getIndex());
                tasks.get(i - 1).setIndex(temp);
            }

        } else {

            for (int i = 0; i < tasks.size() - 1; i++) {
                int temp = tasks.get(i).getIndex();
                tasks.get(i).setIndex(tasks.get(i + 1).getIndex());
                tasks.get(i + 1).setIndex(temp);
            }

        }

        return tasks;
    }
}
