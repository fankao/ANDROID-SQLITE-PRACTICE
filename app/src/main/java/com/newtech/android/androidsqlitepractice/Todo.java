package com.newtech.android.androidsqlitepractice;

import android.content.Intent;

import java.util.UUID;

public class Todo {
    private UUID id;
    private String name;
    private boolean isCompleted;

    public Todo() {
    }

    public Todo(String name, boolean isCompleted) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.isCompleted = isCompleted;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
