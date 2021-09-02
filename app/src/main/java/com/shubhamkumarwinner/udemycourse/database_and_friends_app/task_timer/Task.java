package com.shubhamkumarwinner.udemycourse.database_and_friends_app.task_timer;

import java.io.Serializable;

public class Task implements Serializable {
    public static final long serialVersionUID = 20210902L;

    private long id;
    private final String name;
    private final String description;
    private final int sortOrder;

    public Task(long id, String name, String description, int sortOrder) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sortOrder = sortOrder;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", sortOrder=" + sortOrder +
                '}';
    }
}
